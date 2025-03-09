package com.financeTracker.service;

import com.financeTracker.model.entity.Budget;
import com.financeTracker.model.entity.Transaction;

import java.util.Optional;

public interface BudgetService {

    void createBudget(Long userId, double limit);

    Optional<Budget> getBudget(long userId);

    void updateBudget(Budget budget);

    void deleteBudget(long userId);

    boolean isBudgetExceeded(long userId);
}
