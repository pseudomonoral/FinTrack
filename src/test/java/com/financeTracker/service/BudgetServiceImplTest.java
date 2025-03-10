package ru.polskiy.service;

import com.financeTracker.dao.BudgetDao;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.mapper.BudgetMapper;
import com.financeTracker.model.entity.Budget;
import com.financeTracker.service.impl.BudgetServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.polskiy.constants.TestConstants.BUDGET;

@ExtendWith(MockitoExtension.class)
class BudgetServiceImplTest {

    @Mock
    private BudgetDao budgetDao;

    @Mock
    private BudgetMapper budgetMapper;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    @Test
    @DisplayName("Should save budget when created")
    void createBudget_ShouldSaveBudget() {
        when(budgetMapper.mapToBudget(BUDGET.getUserId(), BUDGET.getLimit())).thenReturn(BUDGET);
        budgetService.createBudget(BUDGET.getUserId(),BUDGET.getLimit());

        verify(budgetDao, times(1)).save(BUDGET);
    }

    @Test
    @DisplayName("Should return budget when it exists")
    void getBudget_ShouldReturnBudget_WhenExists() {
        when(budgetDao.findById(BUDGET.getUserId())).thenReturn(Optional.of(BUDGET));
        Budget result = budgetService.getBudget(BUDGET.getUserId());

        assertNotNull(result);
        assertThat(BUDGET.getUserId()).isEqualTo(result.getUserId());
    }

    @Test
    @DisplayName("Should throw exception when budget is not found")
    void getBudget_ShouldThrowException_WhenNotFound() {
        when(budgetDao.findById(BUDGET.getUserId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> budgetService.getBudget(BUDGET.getUserId()));
    }

    @Test
    @DisplayName("Should update budget when it exists")
    void updateBudget_ShouldUpdate_WhenExists() {
        when(budgetDao.findByUserId(BUDGET.getUserId())).thenReturn(Optional.of(BUDGET));
        when(budgetMapper.mapToBudget(BUDGET.getUserId(), BUDGET.getLimit())).thenReturn(BUDGET);

        budgetService.updateBudget(BUDGET.getUserId(),BUDGET.getLimit());

        verify(budgetDao, times(1)).updateBudget(BUDGET);
    }

    @Test
    @DisplayName("Should throw exception when trying to update a non-existing budget")
    void updateBudget_ShouldThrowException_WhenNotFound() {
        when(budgetDao.findByUserId(BUDGET.getUserId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> budgetService
                .updateBudget(BUDGET.getUserId(), BUDGET.getLimit()));
    }

    @Test
    @DisplayName("Should delete budget when it exists")
    void deleteBudget_ShouldDelete_WhenExists() {
        when(budgetDao.findById(BUDGET.getUserId())).thenReturn(Optional.of(BUDGET));

        budgetService.deleteBudget(BUDGET.getUserId());

        verify(budgetDao, times(1)).delete(BUDGET);
    }

    @Test
    @DisplayName("Should throw exception when trying to delete a non-existing budget")
    void deleteBudget_ShouldThrowException_WhenNotFound() {
        when(budgetDao.findById(BUDGET.getUserId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> budgetService.deleteBudget(BUDGET.getUserId()));
    }

    @Test
    @DisplayName("Should return true when budget limit is not zero")
    void isBudgetExceeded_ShouldReturnTrue_WhenLimitNotZero() {
        when(budgetDao.findByUserId(BUDGET.getUserId())).thenReturn(Optional.of(BUDGET));
        assertTrue(budgetService.isBudgetExceeded(BUDGET.getUserId()));
    }

    @Test
    @DisplayName("Should throw exception when budget is not found")
    void isBudgetExceeded_ShouldReturnFalse_WhenBudgetNotFound() {
        when(budgetDao.findByUserId(BUDGET.getUserId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> budgetService.isBudgetExceeded(BUDGET.getUserId()));
    }
}
