package ru.polskiy.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.polskiy.constants.TestConstants.GOAL;
import static ru.polskiy.constants.TestConstants.USER;

import com.financeTracker.ApplicatoinContext;
import com.financeTracker.dao.GoalDao;
import com.financeTracker.exceptions.EntityAlreadyExistException;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.mapper.GoalMapper;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.service.impl.GoalServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GoalServiceImplTest {

    @Mock
    private GoalDao goalDao;

    @Mock
    private GoalMapper goalMapper;

    @InjectMocks
    private GoalServiceImpl goalService;

    @Test
    @DisplayName("Should save goal when it does not exist")
    void createGoal_ShouldSaveGoal_WhenNotExists() {
        when(goalDao.findByUserId(GOAL.getUserId())).thenReturn(Optional.empty());
        when(goalMapper.mapToGoal(GOAL.getUserId(), GOAL.getName(), GOAL.getTargetAmount()))
                .thenReturn(GOAL);
        when(goalDao.save(GOAL)).thenReturn(GOAL);

        Goal result = goalService.createGoal(GOAL.getUserId(), false, GOAL.getName(),GOAL.getTargetAmount());

        assertNotNull(result);
        assertThat(GOAL.getName()).isEqualTo(result.getName());
        verify(goalDao, times(1)).save(GOAL);
    }

    @Test
    @DisplayName("Should throw exception when goal already exists")
    void createGoal_ShouldThrowException_WhenAlreadyExists() {
        when(goalDao.findByUserId(GOAL.getUserId())).thenReturn(Optional.of(GOAL));

        assertThrows(EntityAlreadyExistException.class, () -> goalService.createGoal(GOAL.getUserId(), false,
                GOAL.getName(), GOAL.getTargetAmount()));
    }

    @Test
    @DisplayName("Should return goal when it exists")
    void getGoal_ShouldReturnGoal_WhenExists() {
        when(goalDao.findByUserId(GOAL.getUserId())).thenReturn(Optional.of(GOAL));
        Goal result = goalService.getGoal(GOAL.getUserId());

        assertNotNull(result);
        assertEquals(GOAL.getName(), result.getName());
    }

    @Test
    @DisplayName("Should throw exception when goal is not found")
    void getGoal_ShouldThrowException_WhenNotFound() {
        when(goalDao.findByUserId(GOAL.getUserId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> goalService.getGoal(GOAL.getUserId()));
    }

    @Test
    @DisplayName("Should delete goal when it exists")
    void deleteGoal_ShouldDeleteGoal_WhenExists() {
        when(goalDao.findByUserId(GOAL.getUserId())).thenReturn(Optional.of(GOAL));

        goalService.deleteGoal(GOAL.getUserId());

        verify(goalDao, times(1)).delete(GOAL);
    }

    @Test
    @DisplayName("Should throw exception when trying to delete a non-existing goal")
    void deleteGoal_ShouldThrowException_WhenNotFound() {
        when(goalDao.findByUserId(GOAL.getUserId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> goalService.deleteGoal(GOAL.getUserId()));
    }

    @Test
    @DisplayName("Should update goal when it exists")
    void updateGoal_ShouldUpdateGoal_WhenExists() {
        when(goalDao.findByUserId(GOAL.getUserId())).thenReturn(Optional.of(GOAL));
        when(goalMapper.mapToGoal(GOAL.getUserId(), GOAL.getName(), GOAL.getTargetAmount())).thenReturn(GOAL);

        goalService.updateGoal(GOAL.getUserId(), GOAL.getName(), GOAL.getTargetAmount());

        verify(goalDao, times(1)).updateGoal(GOAL);
    }

    @Test
    @DisplayName("Should throw exception when trying to update a non-existing goal")
    void updateGoal_ShouldThrowException_WhenNotFound() {
        when(goalDao.findByUserId(GOAL.getUserId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> goalService.updateGoal(GOAL.getUserId(), GOAL.getName(),
                GOAL.getTargetAmount()));
    }

    @Test
    @DisplayName("Should return correct progress when goal is not completed")
    void getGoalProgress_ShouldReturnCorrectProgress_WhenGoalNotCompleted() {
        when(ApplicatoinContext.getAuthorizeUser()).thenReturn(USER);
        when(goalDao.findByUserId(GOAL.getUserId())).thenReturn(Optional.of(GOAL));

        double progress = goalService.getGoalProgress();

        assertEquals(3000.0, progress);
    }

    @Test
    @DisplayName("Should throw exception when goal is completed")
    void getGoalProgress_ShouldThrowException_WhenGoalCompleted() {
        GOAL.setGoalCompleted(true);
        when(ApplicatoinContext.getAuthorizeUser()).thenReturn(USER);
        when(goalDao.findByUserId(GOAL.getUserId())).thenReturn(Optional.of(GOAL));

        assertThrows(RuntimeException.class, () -> goalService.getGoalProgress());
    }
}

