package com.financeTracker.mapper;

import com.financeTracker.model.entity.Goal;

public class EntityGoalMapper {

    public Goal mapToGoal(Long userId, String nameGoal, double targetAmount, double currentAmount,
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
