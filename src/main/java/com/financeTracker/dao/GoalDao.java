package com.financeTracker.dao;

import com.financeTracker.model.entity.Goal;

import java.util.List;
import java.util.Optional;

/**
 * The GoalDao interface extends MainDao to provide data access operations specific to Goal entities.
 * It adds additional methods for updating a goal, finding goals by user ID, and finding a goal by name and user ID.
 *
 */
public interface GoalDao {

    /**
     * Updates an existing goal in the database.
     *
     * @param goal The goal entity containing updated data.
     * @throws IllegalArgumentException if the goal does not exist or contains invalid data.
     */
    void updateGoal(Goal goal);

    /**
     * Retrieves all goals associated with a specific user ID.
     *
     * @param userId The ID of the user whose goals are to be retrieved.
     * @return List<Goal> A list of goals associated with the given user ID. If no goals exist, returns an empty list.
     * @throws IllegalArgumentException if the provided userId is null.
     */
    List<Goal> findAllByUserId(Long userId);

    /**
     * Finds a goal by its name and associated user ID.
     *
     * @param goalName The name of the goal.
     * @param userId The ID of the user to whom the goal belongs.
     * @return Optional<Goal> An Optional containing the goal if found, or an empty Optional if not found.
     * @throws IllegalArgumentException if goalName is null or empty, or if userId is null.
     */
    Optional<Goal> findGoalByNameAndId(String goalName, Long userId);

    /**
     * Finds a goal by its unique identifier.
     *
     * @param id The unique identifier of the goal.
     * @return Optional<Goal> An Optional containing the goal if found, or an empty Optional if not found.
     * @throws IllegalArgumentException if the provided ID is null.
     */
    Optional<Goal> findById(Long id);

    /**
     * Retrieves a list of all goals in the database.
     *
     * @return List<Goal> A list of all goals. If no goals exist, returns an empty list.
     */
    List<Goal> findAll();

    /**
     * Saves a new goal or updates an existing one in the database.
     *
     * @param goal The goal entity to be saved or updated.
     * @return Goal The saved goal entity with its assigned ID.
     * @throws IllegalArgumentException if the goal or its required fields are null.
     */
    Goal save(Goal goal);

    /**
     * Deletes a goal from the database by its ID.
     *
     * @param goalId The unique identifier of the goal to be deleted.
     * @throws IllegalArgumentException if the provided goalId is null.
     */
    void delete(Long goalId);
}

