package com.financeTracker.service.impl;

import com.financeTracker.dao.BudgetDao;
import com.financeTracker.dao.TransactionDao;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.mapper.BudgetMapper;
import com.financeTracker.model.entity.Budget;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.service.BudgetService;
import com.financeTracker.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.financeTracker.model.type.EntityName.BUDGET;
import static com.financeTracker.model.type.TransactionType.*;

@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetDao budgetDao;
    private final TransactionDao transactionDao;
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
        Budget newBudget = budgetMapper.mapToBudget(userId, limit);
        Optional<Budget> oldBudget = budgetDao.findByUserId(userId);
        if (oldBudget.isPresent()) {
            newBudget.setId(oldBudget.get().getId());
            budgetDao.updateBudget(newBudget);
        } else {
            throw new EntityNotFoundException(BUDGET, newBudget.getUserId());
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

        double actualExpenses = transactionDao.findAll().stream()
                .filter(transaction -> Objects.equals(transaction.getUserId(), userId))
                .filter(transaction -> !transaction.getCreatedAt().isBefore(oneMonthAgo))
                .filter(transaction -> transaction.getType().equals(EXPENSE))
                .mapToDouble(Transaction::getAmount)
                .sum();

        return actualExpenses > budget.getLimit();
    }

}
