package com.financeTracker.service;

import com.financeTracker.model.entity.Budget;

public interface BudgetService {

    void addBudget(Budget budget);

    Budget getBudget(long userId);

    void setBudget(Budget budget);

    void deleteBudget(long userId);

    boolean isBudgetExceeded(long userId);
}
