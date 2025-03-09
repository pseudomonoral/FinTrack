package com.financeTracker.service;

import com.financeTracker.model.entity.Goal;

import java.util.Optional;

public interface GoalService {

    Goal createGoal(Long userId, boolean goalCompleted, String name, double targetAmount);

    Goal getGoal(String nameGoal, Long userId);

    void deleteGoal(String nameGoal, Long userId);

    void updateGoal(Long userId, String nameGoal, double targetAmount,
                    boolean goalActual);

    Optional<Goal> getActualGoalByUserId(Long userId);

    void deactivateGoalByUserId(Long userId);

    double getGoalProgress(Goal goal);
}
