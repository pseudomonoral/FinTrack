package com.financeTracker.service;

import com.financeTracker.model.entity.Goal;

public interface GoalService {

    Goal createGoal(Long userId, boolean goalCompleted, String name, double targetAmount);

    Goal getGoal(long userId);

    void setGoal(Goal Goal);

    void deleteGoal(long userId);

    double getGoalProgress();
}
