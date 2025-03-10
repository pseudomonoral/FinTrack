package com.financeTracker.service.impl;

import com.financeTracker.dao.GoalDao;
import com.financeTracker.exceptions.entity.EntityAlreadyExistException;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.mapper.GoalMapper;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.service.GoalService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.financeTracker.model.type.EntityName.*;

@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalDao goalDao;
    private final GoalMapper mapper;

    @Override
    public Goal createGoal(Long userId, boolean goalActual, String nameGoal,
                           double targetAmount) {
        if (goalDao.findGoalByNameAndId(nameGoal, userId).isPresent()) {
            throw new EntityAlreadyExistException(GOAL, userId);
        }
        Goal goal = mapper.mapToGoal(userId, nameGoal, targetAmount);
        if (goalActual){
            deactivateGoalByUserId(userId);
        }
        goal.setGoalActual(goalActual);
        goal.onCreate();
        return goalDao.save(goal);
    }

    @Override
    public Goal getGoal(String nameGoal, Long userId) {
        return goalDao.findGoalByNameAndId(nameGoal, userId)
                .orElseThrow(() -> new EntityNotFoundException(GOAL, userId));
    }

    @Override
    public List<Goal> getAllGoals(Long userId) {
        return goalDao.findAllByUserId(userId);
    }

    @Override
    public void deleteGoal(String nameGoal, Long userId) {
        Goal goal = goalDao.findGoalByNameAndId(nameGoal, userId)
                .orElseThrow(() -> new EntityNotFoundException(GOAL, userId));
        goalDao.delete(goal);
    }

    @Override
    public void updateGoal(Long userId, String nameGoal, double targetAmount,
                           boolean goalActual) {
        Goal goal = mapper.mapToGoal(userId, nameGoal, targetAmount);
        goal.setGoalActual(goalActual);

        Optional<Goal> old = goalDao.findGoalByNameAndId(nameGoal, userId);
        if (old.isPresent()) {
            if (goalActual && !old.get().isGoalActual()){
                deactivateGoalByUserId(userId);
            }
            goalDao.updateGoal(goal);
        } else {
            throw new EntityNotFoundException(GOAL, goal.getUserId());
        }
    }

    @Override
    public Optional<Goal> getActualGoalByUserId(Long userId) {
        return goalDao.findAllByUserId(userId).stream()
                .filter(Goal::isGoalActual).
                findFirst();
    }

    @Override
    public void deactivateGoalByUserId(Long userId){
        Goal goal = getActualGoalByUserId(userId).
                orElseThrow(() -> new EntityNotFoundException(GOAL, userId));
        goal.setGoalActual(false);
        goalDao.updateGoal(goal);
    }

    @Override
    public double getGoalProgress(Goal goal) {
        if (goal.getTargetAmount() == 0) {
            return 100.0;
        }
        return Math.min((goal.getCurrentAmount() / goal.getTargetAmount()) * 100, 100.0);
    }
}
