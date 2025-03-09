package com.financeTracker.service.impl;

import com.financeTracker.dao.BudgetDao;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.mapper.BudgetMapper;
import com.financeTracker.model.entity.Budget;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.service.BudgetService;
import com.financeTracker.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.financeTracker.model.type.EntityName.BUDGET;
import static com.financeTracker.model.type.TransactionType.*;

@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetDao budgetDao;
    private final TransactionService transactionService;
    private final BudgetMapper budgetMapper;

    @Override
    public void createBudget(Long userId, double limit) {
        Budget budget = budgetMapper.mapToBudget(userId, limit);

        budget.onCreate();
        budgetDao.save(budget);
    }

    @Override
    public Optional<Budget> getBudget(long userId) {
        return budgetDao.findByUserId(userId);
    }

    @Override
    public void updateBudget(Long userId, double limit) {
        Budget budget = budgetMapper.mapToBudget(userId, limit);

        if (budgetDao.findByUserId(userId).isPresent()) {
            budgetDao.updateBudget(budget);
        } else {
            throw new EntityNotFoundException(BUDGET, budget.getUserId());
        }
    }

    @Override
    public void deleteBudget(long userId) {
        Budget budget = budgetDao.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(BUDGET, userId));
        budgetDao.delete(budget);
    }

    @Override
    public boolean isBudgetExceeded(long userId) {
        Budget budget = budgetDao.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(BUDGET, userId));

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        double actualExpenses = transactionService.getTransactionsByDate(userId, oneMonthAgo)
                .stream()
                .filter(transaction -> transaction.getType().equals(EXPENSE))
                .mapToDouble(Transaction::getAmount)
                .sum();

        return actualExpenses > budget.getLimit();
    }

}
