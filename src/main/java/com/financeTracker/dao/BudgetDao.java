package com.financeTracker.dao;

import com.financeTracker.model.entity.Budget;

import java.util.Optional;


/**
 * The BudgetDao interface extends MainDao to provide data access operations specific to Budget entities.
 * It adds additional methods for updating a budget and finding a budget by user ID.
 *
 * @see MainDao
 */
public interface BudgetDao extends MainDao<Long, Budget> {

    /**
     * Updates a budget in the database.
     *
     * @param budget The budget entity to be updated.
     * @return Budget The updated budget entity.
     */
    Budget updateBudget(Budget budget);

    /**
     * Finds a budget by the user ID.
     *
     * @param userId The ID of the user whose budget is to be retrieved.
     * @return Optional<Budget> An Optional containing the budget if found, or an empty Optional if not found.
     */
    Optional<Budget> findByUserId(Long userId);
}

