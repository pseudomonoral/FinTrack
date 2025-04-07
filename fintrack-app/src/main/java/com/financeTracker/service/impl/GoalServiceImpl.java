package com.financeTracker.service.impl;

import com.financeTracker.dao.GoalDao;
import com.financeTracker.dto.GoalRequestDto;
import com.financeTracker.dto.GoalResponseDto;
import com.financeTracker.exceptions.entity.EntityAlreadyExistException;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.mapper.GoalMapper;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.financeTracker.model.type.EntityName.GOAL;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalDao goalDao;
    private final GoalMapper goalMapper;

    @Transactional
    @Override
    public GoalResponseDto createGoal(Long userId, GoalRequestDto goalRequestDto) {
        if (goalDao.findGoalByNameAndId(goalRequestDto.getName(), userId).isPresent()) {
            throw new EntityAlreadyExistException(GOAL, userId);
        }
        if (goalRequestDto.getGoalActive()) {
            deactivateGoalByUserId(userId);
        }

        Goal goal = mapToGoal(userId,
                goalRequestDto.getName(),
                goalRequestDto.getTargetAmount(),
                goalRequestDto.getCurrentAmount(),
                goalRequestDto.getGoalActive());
        goal.onCreate();

        goalDao.save(goal);
        return goalMapper.toGoalResponseDto(goalDao.findById(goal.getId())
                .orElseThrow(() -> new EntityNotFoundException(GOAL, userId)));
    }

    @Override
    public GoalResponseDto getGoal(GoalResponseDto goalResponseDto, Long userId) {
        return goalMapper.toGoalResponseDto(goalDao.findGoalByNameAndId(goalResponseDto.getName(),
                        userId)
                .orElseThrow(() -> new EntityNotFoundException(GOAL, userId)));
    }

    @Override
    public List<GoalResponseDto> getAllGoals(Long userId) {
        return goalDao.findAllByUserId(userId)
                .stream().map(goalMapper::toGoalResponseDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteGoal(GoalRequestDto requestDto, Long userId) {
        Goal goal = goalDao.findGoalByNameAndId(requestDto.getName(), userId)
                .orElseThrow(() -> new EntityNotFoundException(GOAL, userId));
        goalDao.delete(goal.getId());
    }

    @Transactional
    @Override
    public void updateGoal(Long userId, GoalRequestDto goalRequestDto) {
        Goal goal = mapToGoal(userId,
                goalRequestDto.getName(),
                goalRequestDto.getTargetAmount(),
                goalRequestDto.getCurrentAmount(),
                goalRequestDto.getGoalActive());

        goalDao.findGoalByNameAndId(goal.getName(), userId).ifPresentOrElse(existingGoal -> {
            if (goal.isGoalActual() && !existingGoal.isGoalActual()) {
                deactivateGoalByUserId(userId);
            }
            goal.setId(existingGoal.getId());
            goalDao.updateGoal(goal);
        }, () -> {
            throw new EntityNotFoundException(GOAL, userId);
        });
    }

    @Override
    public Optional<GoalResponseDto> getActualGoalByUserId(Long userId) {
        return goalDao.findAllByUserId(userId).stream()
                .filter(Goal::isGoalActual)
                .findFirst()
                .map(goalMapper::toGoalResponseDto);
    }

    @Transactional
    @Override
    public void deactivateGoalByUserId(Long userId) {
        goalDao.findAllByUserId(userId).stream()
                .filter(Goal::isGoalActual)
                .findFirst().ifPresent(goal -> {
            goal.setGoalActual(false);
            goalDao.updateGoal(goal);
        });
    }

    private Goal mapToGoal(Long userId, String nameGoal, double targetAmount, double currentAmount,
                           boolean goalActual) {
        return Goal.builder()
                .userId(userId)
                .name(nameGoal)
                .currentAmount(currentAmount)
                .goalActual(goalActual)
                .targetAmount(targetAmount)
                .build();
    }
}

