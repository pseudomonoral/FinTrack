package com.financeTracker.controller;

import com.financeTracker.dto.GoalCreateRequestDTO;
import com.financeTracker.dto.GoalResponseDTO;
import com.financeTracker.dto.GoalUpdateRequestDTO;
import com.financeTracker.mapper.GoalMapper;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.service.GoalService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;

import java.util.List;

@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;
    private final GoalMapper goalMapper = Mappers.getMapper(GoalMapper.class);

    public GoalResponseDTO createGoal(Long userId, GoalCreateRequestDTO dto) {
        Goal goal = goalService.createGoal(userId, dto.getGoalActive(), dto.getName(), dto.getTargetAmount(), 0);
        return goalMapper.toDTO(goal);
    }

    public List<GoalResponseDTO> getGoals(Long userId) {
        return goalService.getAllGoals(userId)
                .stream()
                .map(goalMapper::toDTO)
                .toList();
    }

    public GoalResponseDTO getGoal(String nameGoal, Long userId) {
        Goal goal = goalService.getGoal(nameGoal, userId);
        return goalMapper.toDTO(goal);
    }

    public void deleteGoal(String nameGoal, Long userId) {
        goalService.deleteGoal(nameGoal, userId);
    }

    public GoalResponseDTO updateGoal(Long userId, GoalUpdateRequestDTO dto) {
        goalService.updateGoal(dto.getId(), userId, dto.getName(), dto.getTargetAmount(), 0, dto.getGoalActual());
        Goal updatedGoal = goalService.getGoal(dto.getName(), userId);
        return goalMapper.toDTO(updatedGoal);
    }
}


