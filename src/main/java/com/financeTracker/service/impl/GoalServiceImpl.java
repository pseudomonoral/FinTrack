package com.financeTracker.service.impl;

import com.financeTracker.dao.GoalDao;
import com.financeTracker.exception.EntityNotFoundException;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.service.GoalService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.financeTracker.model.type.EntityName.*;

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
    public Optional<Goal> getGoal(long userId) {
        return goalDao.findById(userId);
    }

    @Override
    public void deleteGoal(long userId) {
        Goal goal = goalDao.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(GOAL, userId));
        goalDao.delete(goal);
    }

    @Override
    public void updateGoal(Goal goal) {
        if (goalDao.findById(goal.getUserId()).isPresent()) {
            goalDao.updateGoal(goal);
        }else {
            throw new EntityNotFoundException(GOAL, goal.getUserId());
        }
    }

    @Override
    public double getGoalProgress() {
        return 0;
        //TODO: взаимодействие с сервисом транзакций
    }
}
