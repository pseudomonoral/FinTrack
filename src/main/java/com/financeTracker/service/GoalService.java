package com.financeTracker.service;

import com.financeTracker.model.entity.Goal;

import java.util.List;

public interface GoalService {

    void addBudget(Goal goal);

    Goal getGoal(long userId);

    void setGoal(Goal Goal);

    void deleteGoal(long userId);

    double getGoalProgress();

    List<Goal> getArchivedGoals();

    void cleanArchivedGoals();
}
