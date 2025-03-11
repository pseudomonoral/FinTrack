package com.financeTracker.service;

import com.financeTracker.model.entity.Goal;

import java.util.List;
import java.util.Optional;

/**
 * The GoalService interface provides the operations for managing user goals.
 * It includes methods for creating, retrieving, updating, deleting goals, and checking goal progress.
 */
public interface GoalService {

    /**
     * Creates a new goal for a specific user.
     *
     * @param userId The ID of the user who is setting the goal.
     * @param goalActive A boolean indicating whether the goal is active.
     * @param name The name of the goal.
     * @param targetAmount The target amount for the goal.
     * @return Goal The created goal entity.
     */
    Goal createGoal(Long userId, boolean goalActive, String name, double targetAmount, double currentAmount);

    /**
     * Retrieves a specific goal by its name and associated user ID.
     *
     * @param nameGoal The name of the goal.
     * @param userId The ID of the user whose goal is to be retrieved.
     * @return Goal The retrieved goal entity.
     */
    Goal getGoal(String nameGoal, Long userId);

    /**
     * Retrieves all goals associated with a specific user.
     *
     * @param userId The ID of the user whose goals are to be retrieved.
     * @return List<Goal> A list of goals associated with the given user ID.
     */
    List<Goal> getAllGoals(Long userId);

    /**
     * Deletes a specific goal by its name and associated user ID.
     *
     * @param nameGoal The name of the goal to be deleted.
     * @param userId The ID of the user whose goal is to be deleted.
     */
    void deleteGoal(String nameGoal, Long userId);

    /**
     * Updates a specific goal for a user with new target amount and completion status.
     *
     * @param userId The ID of the user whose goal is to be updated.
     * @param nameGoal The name of the goal to be updated.
     * @param targetAmount The new target amount for the goal.
     * @param goalActual A boolean indicating whether the goal is actually achieved.
     */
    void updateGoal(Long id, Long userId, String nameGoal, double targetAmount, double currentAmount, boolean goalActual);

    /**
     * Retrieves the current goal that is active for a specific user.
     *
     * @param userId The ID of the user whose active goal is to be retrieved.
     * @return Optional<Goal> An Optional containing the active goal if found, or an empty Optional if not found.
     */
    Optional<Goal> getActualGoalByUserId(Long userId);

    /**
     * Deactivates the current goal of a specific user.
     *
     * @param userId The ID of the user whose active goal is to be deactivated.
     */
    void deactivateGoalByUserId(Long userId);

    /**
     * Calculates the progress made towards achieving a specific goal.
     *
     * @param goal The goal for which progress needs to be calculated.
     * @return double The progress made towards the target amount, expressed as a percentage.
     */
    double getGoalProgress(Goal goal);
}

