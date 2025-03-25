package com.financeTracker.service.impl;

import com.financeTracker.dao.GoalDao;
import com.financeTracker.exceptions.entity.EntityAlreadyExistException;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.service.GoalService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.financeTracker.model.type.EntityName.*;

@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalDao goalDao;

    @Override
    public Goal createGoal(Long userId, boolean goalActual, String nameGoal,
                           double targetAmount, double currentAmount) {
        if (goalDao.findGoalByNameAndId(nameGoal, userId).isPresent()) {
            throw new EntityAlreadyExistException(GOAL, userId);
        }
        Goal goal = mapToGoal(userId, nameGoal, targetAmount, currentAmount, goalActual);
        goal.onCreate();
        if (goalActual){
            deactivateGoalByUserId(userId);
        }
        goalDao.save(goal);
        return goalDao.findById(goal.getId())
                .orElseThrow(() -> new EntityNotFoundException(GOAL, userId));
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
        goalDao.delete(goal.getId());
    }

    @Override
    public void updateGoal(Long id, Long userId, String nameGoal, double targetAmount, double currentAmount,
                           boolean goalActual) {
        Goal goal = mapToGoal(userId, nameGoal, targetAmount, currentAmount, goalActual);
        goal.setId(id);

        Optional<Goal> oldActualGoal = goalDao.findGoalByNameAndId(nameGoal, userId);
        if (oldActualGoal.isPresent()) {
            if (goalActual && !oldActualGoal.get().isGoalActual()){
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
        Optional<Goal> goal = getActualGoalByUserId(userId);
        if (goal.isPresent()){
            Goal goalForUpdate = goal.get();
            goalForUpdate.setGoalActual(false);
            goalDao.updateGoal(goalForUpdate);
        }

    }

    @Override
    public double getGoalProgress(Goal goal) {
        if (goal.getTargetAmount() == 0) {
            return 100.0;
        }
        return Math.min((goal.getCurrentAmount() / goal.getTargetAmount()) * 100, 100.0);
    }

    private Goal mapToGoal(Long userId, String nameGoal, double targetAmount, double currentAmount,
                           boolean goalActual){
        return Goal.builder()
                .userId(userId)
                .name(nameGoal)
                .currentAmount(currentAmount)
                .goalActual(goalActual)
                .targetAmount(targetAmount)
                .build();
    }
}
