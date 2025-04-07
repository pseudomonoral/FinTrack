package com.financeTracker.service.impl;

import com.financeTracker.dao.BudgetDao;
import com.financeTracker.dao.TransactionDao;
import com.financeTracker.dto.BudgetCheckResponseDto;
import com.financeTracker.dto.BudgetDto;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.mapper.BudgetMapper;
import com.financeTracker.model.entity.Budget;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.financeTracker.model.type.EntityName.BUDGET;
import static com.financeTracker.model.type.TransactionType.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BudgetServiceImpl implements BudgetService {

    private final BudgetDao budgetDao;
    private final TransactionDao transactionDao;
    private final BudgetMapper budgetMapper;

    @Transactional
    @Override
    public void createBudget(Long userId, BudgetDto budgetDto) {
        Budget budget = mapToBudget(userId, budgetDto.getLimit());

        budget.onCreate();
        budgetDao.save(budget);
    }

    @Override
    public Optional<BudgetDto> getBudget(long userId) {
        return budgetDao.findByUserId(userId)
                .map(budgetMapper::toBudgetDto);
    }

    @Transactional
    @Override
    public void updateBudget(Long userId, BudgetDto budgetDto) {
        Budget newBudget = mapToBudget(userId, budgetDto.getLimit());
        Optional<Budget> oldBudget = budgetDao.findByUserId(userId);
        if (oldBudget.isPresent()) {
            newBudget.setId(oldBudget.get().getId());
            budgetDao.updateBudget(newBudget);
        } else {
            throw new EntityNotFoundException(BUDGET, newBudget.getUserId());
        }
    }

    @Transactional
    @Override
    public void deleteBudget(long userId) {
        Budget budget = budgetDao.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(BUDGET, userId));
        budgetDao.delete(budget.getId());
    }

    @Override
    public BudgetCheckResponseDto isBudgetExceeded(long userId) {
        Budget budget = budgetDao.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(BUDGET, userId));

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        double actualExpenses = transactionDao.findAll().stream()
                .filter(transaction -> Objects.equals(transaction.getUserId(), userId))
                .filter(transaction -> !transaction.getCreatedAt().isBefore(oneMonthAgo))
                .filter(transaction -> transaction.getType().equals(EXPENSE))
                .mapToDouble(Transaction::getAmount)
                .sum();

        return BudgetCheckResponseDto.builder()
                .budgetLimit(budget.getLimit())
                .monthlyExpense(actualExpenses)
                .exceeded(actualExpenses > budget.getLimit())
                .build();
    }
    private Budget mapToBudget(Long userId, double limit) {
        return Budget.builder()
                .userId(userId)
                .limit(limit)
                .build();
    }
}
