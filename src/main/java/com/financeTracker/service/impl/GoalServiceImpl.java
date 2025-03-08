package com.financeTracker.service.impl;

import com.financeTracker.dao.GoalDao;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.service.GoalService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalDao goalDao;

    @Override
    public Goal createGoal(Long userId, boolean goalCompleted,
                           String name, double targetAmount) {
        Goal goal = Goal.builder()
                .userId(userId)
                .name(name)
                .targetAmount(targetAmount)
                .build();

        goal.onCreate();
        return goalDao.save(goal);
    }

    @Override
    public Goal getGoal(long userId) {
        return null;
    }

    @Override
    public void setGoal(Goal Goal) {

    }

    @Override
    public void deleteGoal(long userId) {

    }

    @Override
    public double getGoalProgress() {
        return 0;
    }
}
