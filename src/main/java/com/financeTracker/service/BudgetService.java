package com.financeTracker.service;

import com.financeTracker.model.entity.Budget;

import java.util.Optional;

/**
 * The BudgetService interface provides the operations for managing a user's budget.
 * It includes methods for creating, retrieving, updating, deleting a budget, and checking if the budget is exceeded.
 */
public interface BudgetService {

    /**
     * Creates a budget for a specific user with a given spending limit.
     *
     * @param userId The ID of the user for whom the budget is to be created.
     * @param limit The spending limit for the user's budget.
     */
    void createBudget(Long userId, double limit);

    /**
     * Retrieves the budget for a specific user.
     *
     * @param userId The ID of the user whose budget is to be retrieved.
     * @return Optional<Budget> An Optional containing the user's budget if found, or an empty Optional if not found.
     */
    Optional<Budget> getBudget(long userId);

    /**
     * Updates the budget for a specific user with a new spending limit.
     *
     * @param userId The ID of the user whose budget is to be updated.
     * @param limit The new spending limit for the user's budget.
     */
    void updateBudget(Long userId, double limit);

    /**
     * Deletes the budget for a specific user.
     *
     * @param userId The ID of the user whose budget is to be deleted.
     */
    void deleteBudget(long userId);

    /**
     * Checks if the user's budget has been exceeded.
     *
     * @param userId The ID of the user whose budget is being checked.
     * @return boolean True if the budget has been exceeded, false otherwise.
     */
    boolean isBudgetExceeded(long userId);
}
