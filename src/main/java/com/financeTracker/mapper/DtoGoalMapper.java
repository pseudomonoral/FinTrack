package com.financeTracker.mapper;

import com.financeTracker.dto.GoalResponseDto;
import com.financeTracker.model.entity.Goal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DtoGoalMapper {

    @Mapping(target = "progress", expression = "java(goal.getCurrentAmount() / goal.getTargetAmount() * 100)")
    GoalResponseDto toDTO(Goal goal);
}

