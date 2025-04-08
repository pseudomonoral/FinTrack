package com.financeTracker.service;

import com.financeTracker.dao.GoalDao;
import com.financeTracker.dto.GoalRequestDto;
import com.financeTracker.dto.GoalResponseDto;
import com.financeTracker.mapper.GoalMapper;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.service.impl.GoalServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalServiceImplTest {

    @Mock
    private GoalDao goalDao;

    @Mock
    private GoalMapper goalMapper;

    @InjectMocks
    private GoalServiceImpl goalService;

    private final Long userId = 1L;

    @Test
    void createGoal_shouldSaveAndReturnDto() {
        GoalRequestDto requestDto = GoalRequestDto.builder()
                .name("Travel")
                .targetAmount(1000)
                .currentAmount(100)
                .goalActive(true)
                .build();

        Goal savedGoal = Goal.builder()
                .id(10L)
                .userId(userId)
                .name("Travel")
                .targetAmount(1000)
                .currentAmount(100)
                .goalActual(true)
                .build();

        GoalResponseDto responseDto = GoalResponseDto.builder()
                .id(10L)
                .name("Travel")
                .targetAmount(1000)
                .currentAmount(100)
                .goalActual(true)
                .progress(10.0)
                .build();

        when(goalDao.findGoalByNameAndId(requestDto.getName(), userId)).thenReturn(Optional.empty());
        when(goalDao.findAllByUserId(userId)).thenReturn(Collections.emptyList());
        when(goalDao.save(any())).then(inv -> {
            Goal goal = inv.getArgument(0);
            goal.setId(10L);
            return null;
        });
        when(goalDao.findById(10L)).thenReturn(Optional.of(savedGoal));
        when(goalMapper.toGoalResponseDto(savedGoal)).thenReturn(responseDto);

        GoalResponseDto result = goalService.createGoal(userId, requestDto);

        assertEquals(responseDto, result);
        verify(goalDao).save(any(Goal.class));
    }

    @Test
    void getGoal_shouldReturnDto() {
        GoalResponseDto inputDto = GoalResponseDto.builder().name("Study").build();
        Goal goal = Goal.builder().name("Study").build();
        GoalResponseDto expectedDto = GoalResponseDto.builder().name("Study").build();

        when(goalDao.findGoalByNameAndId("Study", userId)).thenReturn(Optional.of(goal));
        when(goalMapper.toGoalResponseDto(goal)).thenReturn(expectedDto);

        GoalResponseDto result = goalService.getGoal(inputDto, userId);

        assertEquals(expectedDto, result);
    }

    @Test
    void getAllGoals_shouldReturnListOfDto() {
        Goal goal = Goal.builder().name("Health").build();
        GoalResponseDto dto = GoalResponseDto.builder().name("Health").build();

        when(goalDao.findAllByUserId(userId)).thenReturn(List.of(goal));
        when(goalMapper.toGoalResponseDto(goal)).thenReturn(dto);

        List<GoalResponseDto> result = goalService.getAllGoals(userId);

        assertEquals(1, result.size());
        assertEquals("Health", result.get(0).getName());
    }

    @Test
    void deleteGoal_shouldDeleteGoalById() {
        Goal goal = Goal.builder().id(5L).name("Buy car").build();
        GoalRequestDto dto = GoalRequestDto.builder().name("Buy car").build();

        when(goalDao.findGoalByNameAndId("Buy car", userId)).thenReturn(Optional.of(goal));

        goalService.deleteGoal(dto, userId);

        verify(goalDao).delete(5L);
    }

    @Test
    void updateGoal_shouldUpdateExistingGoal() {
        Goal existing = Goal.builder().id(2L).name("Invest").goalActual(false).build();
        GoalRequestDto dto = GoalRequestDto.builder()
                .name("Invest")
                .targetAmount(5000)
                .currentAmount(1000)
                .goalActive(true)
                .build();

        when(goalDao.findGoalByNameAndId("Invest", userId)).thenReturn(Optional.of(existing));
        when(goalDao.findAllByUserId(userId)).thenReturn(Collections.singletonList(existing));

        goalService.updateGoal(userId, dto);

        ArgumentCaptor<Goal> captor = ArgumentCaptor.forClass(Goal.class);
        verify(goalDao).updateGoal(captor.capture());
        Goal updated = captor.getValue();
        assertTrue(updated.isGoalActual());
    }

    @Test
    void getActualGoalByUserId_shouldReturnDto() {
        Goal actual = Goal.builder().name("Trip").goalActual(true).build();
        GoalResponseDto dto = GoalResponseDto.builder().name("Trip").goalActual(true).build();

        when(goalDao.findAllByUserId(userId)).thenReturn(List.of(actual));
        when(goalMapper.toGoalResponseDto(actual)).thenReturn(dto);

        Optional<GoalResponseDto> result = goalService.getActualGoalByUserId(userId);

        assertTrue(result.isPresent());
        assertEquals("Trip", result.get().getName());
    }

    @Test
    void deactivateGoalByUserId_shouldUpdateGoal() {
        Goal activeGoal = Goal.builder().id(7L).goalActual(true).build();
        when(goalDao.findAllByUserId(userId)).thenReturn(List.of(activeGoal));

        goalService.deactivateGoalByUserId(userId);

        assertFalse(activeGoal.isGoalActual());
        verify(goalDao).updateGoal(activeGoal);
    }
}

