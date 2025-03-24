package com.financeTracker.mapper;

import com.financeTracker.dto.GoalResponseDTO;
import com.financeTracker.model.entity.Goal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface GoalMapper {

    @Mapping(target = "progress", expression = "java(goal.getCurrentAmount() / goal.getTargetAmount() * 100)")
    GoalResponseDTO toDTO(Goal goal);
}

