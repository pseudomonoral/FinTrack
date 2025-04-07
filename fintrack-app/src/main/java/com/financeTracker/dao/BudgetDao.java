package com.financeTracker.dao;

import com.financeTracker.model.entity.Budget;

import java.util.List;
import java.util.Optional;


/**
 * The BudgetDao interface extends MainDao to provide data access operations specific to Budget entities.
 * It adds additional methods for updating a budget and finding a budget by user ID.
 *
 */
public interface BudgetDao {

    /**
     * Updates an existing budget in the database.
     *
     * @param budget The budget entity containing updated data.
     * @throws IllegalArgumentException if the budget does not exist.
     */
    void updateBudget(Budget budget);

    /**
     * Finds a budget by the user ID.
     *
     * @param userId The ID of the user whose budget is to be retrieved.
     * @return Optional<Budget> An Optional containing the budget if found, or an empty Optional if not found.
     */
    Optional<Budget> findByUserId(Long userId);

    /**
     * Finds a budget by its unique identifier.
     *
     * @param id The unique identifier of the budget.
     * @return Optional<Budget> An Optional containing the budget if found, or an empty Optional if not found.
     * @throws IllegalArgumentException if the provided ID is null.
     */
    Optional<Budget> findById(Long id);

    /**
     * Retrieves a list of all budgets in the database.
     *
     * @return List<Budget> A list containing all budgets. If no budgets exist, returns an empty list.
     */
    List<Budget> findAll();

    /**
     * Saves a new budget or updates an existing one in the database.
     *
     * @param budget The budget entity to be saved or updated.
     * @return Budget The saved budget entity with its assigned ID.
     * @throws IllegalArgumentException if the budget or its required fields are null.
     */
    Budget save(Budget budget);

    /**
     * Deletes a budget from the database by its ID.
     *
     * @param budgetId The unique identifier of the budget to be deleted.
     * @throws IllegalArgumentException if the provided budgetId is null.
     */
    void delete(Long budgetId);
}


