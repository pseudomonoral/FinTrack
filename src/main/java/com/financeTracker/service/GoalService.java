package com.financeTracker.service;

import com.financeTracker.model.entity.Goal;
import com.financeTracker.model.entity.Transaction;

import java.util.Optional;

public interface GoalService {

    Goal createGoal(Long userId, boolean goalCompleted, String name, double targetAmount);

    Optional<Goal> getGoal(long userId);

    void deleteGoal(long userId);

    void updateGoal(Goal goal);

    double getGoalProgress();
}
