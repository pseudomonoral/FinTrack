package com.financeTracker.dao;

import com.financeTracker.model.entity.Goal;

import java.util.List;
import java.util.Optional;

/**
 * The GoalDao interface extends MainDao to provide data access operations specific to Goal entities.
 * It adds additional methods for updating a goal, finding goals by user ID, and finding a goal by name and user ID.
 *
 * @see MainDao
 */
public interface GoalDao extends MainDao<Long, Goal> {

    /**
     * Updates a goal in the database.
     *
     * @param goal The goal entity to be updated.
     * @return Goal The updated goal entity.
     */
    Goal updateGoal(Goal goal);

    /**
     * Retrieves all goals associated with a specific user ID.
     *
     * @param userId The ID of the user whose goals are to be retrieved.
     * @return List<Goal> A list of goals associated with the given user ID.
     */
    List<Goal> findAllByUserId(Long userId);

    /**
     * Finds a goal by its name and associated user ID.
     *
     * @param goalName The name of the goal.
     * @param userId The ID of the user to whom the goal belongs.
     * @return Optional<Goal> An Optional containing the goal if found, or an empty Optional if not found.
     */
    Optional<Goal> findGoalByNameAndId(String goalName, Long userId);
}
