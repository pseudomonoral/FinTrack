package com.financeTracker.mapper;

import com.financeTracker.dto.GoalResponseDto;
import com.financeTracker.model.entity.Goal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface GoalMapper {
    @Mapping(target = "progress", expression = "java(goal.getTargetAmount() > 0 ? " +
            "(goal.getCurrentAmount() / goal.getTargetAmount() * 100) : 0)")
    GoalResponseDto toGoalResponseDto(Goal goal);
}
