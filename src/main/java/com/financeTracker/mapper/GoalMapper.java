package com.financeTracker.mapper;

import com.financeTracker.model.entity.Goal;

public class GoalMapper {

    public Goal mapToGoal(Long useId, String name, double targetAmount){
        return Goal.builder()
                .userId(useId)
                .name(name)
                .targetAmount(targetAmount)
                .build();
    }
}
