package com.financeTracker.controller;

import com.financeTracker.model.entity.Budget;
import com.financeTracker.service.BudgetService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    /**
     * Creates a budget for the user with a given limit.
     *
     * @param userId The user's ID.
     * @param limit  The limit for the user's budget.
     */
    public void createBudget(Long userId, double limit) {
        budgetService.createBudget(userId, limit);
    }

    /**
     * Gets the current budget of the user.
     *
     * @param userId The user's ID.
     * @return An Optional containing the user's budget if present.
     */
    public Optional<Budget> getBudget(long userId) {
        return budgetService.getBudget(userId);
    }

    /**
     * Updates the user's budget with a new limit.
     *
     * @param userId The user's ID.
     * @param limit  The new limit for the user's budget.
     */
    public void updateBudget(Long userId, double limit) {
        budgetService.updateBudget(userId, limit);
    }

    /**
     * Deletes the user's budget.
     *
     * @param userId The user's ID.
     */
    public void deleteBudget(long userId) {
        budgetService.deleteBudget(userId);
    }

    /**
     * Checks if the user's budget has been exceeded.
     *
     * @param userId The user's ID.
     * @return A boolean indicating whether the budget has been exceeded.
     */
    public boolean isBudgetExceeded(long userId) {
        return budgetService.isBudgetExceeded(userId);
    }
}

