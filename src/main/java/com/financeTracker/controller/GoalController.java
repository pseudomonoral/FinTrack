package com.financeTracker.controller;

import com.financeTracker.model.entity.Goal;
import com.financeTracker.service.GoalService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    /**
     * Creates a goal for the user.
     *
     * @param userId      The user's ID.
     * @param goalCompleted Whether the goal is completed.
     * @param name        The name of the goal.
     * @param targetAmount The target amount for the goal.
     * @return The created goal.
     */
    public Goal createGoal(Long userId, boolean goalCompleted, String name, double targetAmount) {
        return goalService.createGoal(userId, goalCompleted, name, targetAmount);
    }

    public List<Goal> getGoals(Long userId) {
        return goalService.getAllGoals(userId);
    }

    /**
     * Gets the goal of the user by name.
     *
     * @param nameGoal The name of the goal.
     * @param userId   The user's ID.
     * @return The goal for the user.
     */
    public Goal getGoal(String nameGoal, Long userId) {
        return goalService.getGoal(nameGoal, userId);
    }

    /**
     * Deletes the goal of the user by name.
     *
     * @param nameGoal The name of the goal.
     * @param userId   The user's ID.
     */
    public void deleteGoal(String nameGoal, Long userId) {
        goalService.deleteGoal(nameGoal, userId);
    }

    /**
     * Updates the goal of the user.
     *
     * @param userId     The user's ID.
     * @param nameGoal   The name of the goal.
     * @param targetAmount The target amount for the goal.
     * @param goalActual Whether the goal is achieved.
     */
    public void updateGoal(Long userId, String nameGoal, double targetAmount, boolean goalActual) {
        goalService.updateGoal(userId, nameGoal, targetAmount, goalActual);
    }

    /**
     * Gets the current active goal for the user.
     *
     * @param userId The user's ID.
     * @return An Optional containing the active goal for the user.
     */
    public Optional<Goal> getActualGoalByUserId(Long userId) {
        return goalService.getActualGoalByUserId(userId);
    }

    /**
     * Deactivates the goal for the user.
     *
     * @param userId The user's ID.
     */
    public void deactivateGoalByUserId(Long userId) {
        goalService.deactivateGoalByUserId(userId);
    }

    /**
     * Gets the progress of the user's goal.
     *
     * @param goal The goal to check the progress for.
     * @return The progress of the goal.
     */
    public double getGoalProgress(Goal goal) {
        return goalService.getGoalProgress(goal);
    }
}

