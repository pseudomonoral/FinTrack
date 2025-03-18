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
     * Updates a budget in the database.
     *
     * @param budget The budget entity to be updated.
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
     * Finds an entity by its unique identifier.
     *
     * @param id The identifier of the entity.
     * @return Optional<T> An Optional containing the entity if found, or an empty Optional if not found.
     */
    Optional<Budget> findById(Long id);


    /**
     * Retrieves a list of all entities.
     *
     * @return List<T> A list of all entities of type T.
     */
    List<Budget> findAll();

    /**
     * Saves or updates an entity in the database.
     *
     * @param entity The entity to be saved or updated.
     * @return T The saved entity with its updated state (e.g., with an assigned ID).
     */
    Budget save(Budget entity);

    /**
     * Deletes an entity from the database.
     *
     */
    void delete(Long goalId);
}

