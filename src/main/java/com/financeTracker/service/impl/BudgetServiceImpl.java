package com.financeTracker.service.impl;

import com.financeTracker.dao.BudgetDao;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.model.entity.Budget;
import com.financeTracker.service.BudgetService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.financeTracker.model.type.EntityName.BUDGET;

@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetDao budgetDao;

    @Override
    public void createBudget(Long userId, double limit) {
        Budget budget = Budget.builder()
                .userId(userId)
                .limit(limit)
                .build();

        budget.onCreate();
        budgetDao.save(budget);
    }

    @Override
    public Optional<Budget> getBudget(long userId) {
        return budgetDao.findById(userId);
    }

    @Override
    public void updateBudget(Budget budget) {
        if (budgetDao.findById(budget.getUserId()).isPresent()) {
            budgetDao.updateBudget(budget);
        } else {
            throw new EntityNotFoundException(BUDGET, budget.getUserId());
        }
    }

    @Override
    public void deleteBudget(long userId) {
        Budget budget = budgetDao.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(BUDGET, userId));
        budgetDao.delete(budget);
    }

    @Override
    public boolean isBudgetExceeded(long userId) {
        return false;
        //TODO: взаимодействие с сервисом транзакций
    }
}
