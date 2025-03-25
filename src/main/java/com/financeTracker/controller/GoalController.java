package com.financeTracker.controller;

import com.financeTracker.dto.GoalCreateRequestDto;
import com.financeTracker.dto.GoalResponseDto;
import com.financeTracker.dto.GoalUpdateRequestDto;
import com.financeTracker.mapper.DtoGoalMapper;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.service.GoalService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.mapstruct.factory.Mappers;

@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;
    private final DtoGoalMapper dtoGoalMapper = Mappers.getMapper(DtoGoalMapper.class);

    public GoalResponseDto createGoal(Long userId, GoalCreateRequestDto dto) {
        Goal goal = goalService.createGoal(userId, dto.getGoalActive(), dto.getName(), dto.getTargetAmount(), 0);
        return dtoGoalMapper.toDTO(goal);
    }

    public List<GoalResponseDto> getGoals(Long userId) {
        return goalService.getAllGoals(userId)
                .stream()
                .map(dtoGoalMapper::toDTO)
                .toList();
    }

    public GoalResponseDto getGoal(String nameGoal, Long userId) {
        Goal goal = goalService.getGoal(nameGoal, userId);
        return dtoGoalMapper.toDTO(goal);
    }

    public void deleteGoal(String nameGoal, Long userId) {
        goalService.deleteGoal(nameGoal, userId);
    }

    public GoalResponseDto updateGoal(Long userId, GoalUpdateRequestDto dto) {
        goalService.updateGoal(dto.getId(), userId, dto.getName(), dto.getTargetAmount(), 0, dto.getGoalActual());
        Goal updatedGoal = goalService.getGoal(dto.getName(), userId);
        return dtoGoalMapper.toDTO(updatedGoal);
    }
}


