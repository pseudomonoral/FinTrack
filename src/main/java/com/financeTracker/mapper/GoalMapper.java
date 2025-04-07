package com.financeTracker.mapper;

import com.financeTracker.dto.GoalResponseDto;
import com.financeTracker.model.entity.Goal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GoalMapper {
    GoalMapper INSTANCE = Mappers.getMapper(GoalMapper.class);

    @Mapping(target = "progress", source = "goal", qualifiedByName = "calculateProgress")
    GoalResponseDto toGoalResponseDto(Goal goal);

    @Named("calculateProgress")
    static double calculateProgress(Goal goal) {
        return goal.getTargetAmount() > 0 ? (goal.getCurrentAmount() * 100.0 / goal.getTargetAmount()) : 0.0;
    }
}
