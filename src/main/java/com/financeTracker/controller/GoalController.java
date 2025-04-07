package com.financeTracker.controller;

import com.financeTracker.dto.GoalRequestDto;
import com.financeTracker.dto.GoalResponseDto;
import com.financeTracker.mapper.GoalMapper;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.service.GoalService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;
    private final GoalMapper goalMapper = GoalMapper.INSTANCE;

    public GoalResponseDto createGoal(Long userId, GoalRequestDto dto) {
        Goal goal = goalService.createGoal(userId, dto.getGoalActive(), dto.getName(), dto.getTargetAmount(), 0);
        return goalMapper.toGoalResponseDto(goal);
    }

    public List<GoalResponseDto> getGoals(Long userId) {
        return goalService.getAllGoals(userId)
                .stream()
                .map(goalMapper::toGoalResponseDto)
                .toList();
    }

    public GoalResponseDto getGoal(String nameGoal, Long userId) {
        Goal goal = goalService.getGoal(nameGoal, userId);
        return goalMapper.toGoalResponseDto(goal);
    }

    public void deleteGoal(String nameGoal, Long userId) {
        goalService.deleteGoal(nameGoal, userId);
    }

    public GoalResponseDto updateGoal(Long userId, GoalRequestDto dto) {
        goalService.updateGoal(userId, dto.getName(), dto.getTargetAmount(), 0, dto.getGoalActive());
        Goal updatedGoal = goalService.getGoal(dto.getName(), userId);
        return goalMapper.toGoalResponseDto(updatedGoal);
    }
}


