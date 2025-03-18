package com.service;

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

import static com.constants.TestConstants.BUDGET;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    @DisplayName("Should update budget when it exists")
    void updateBudget_ShouldUpdate_WhenExists() {
        when(budgetDao.findByUserId(BUDGET.getUserId())).thenReturn(Optional.of(BUDGET));
        when(budgetMapper.mapToBudget(BUDGET.getUserId(), BUDGET.getLimit())).thenReturn(BUDGET);

        budgetService.updateBudget(BUDGET.getUserId(),BUDGET.getLimit());

        verify(budgetDao, times(1)).updateBudget(BUDGET);
    }


    @Test
    @DisplayName("Should throw exception when budget is not found")
    void isBudgetExceeded_ShouldReturnFalse_WhenBudgetNotFound() {
        when(budgetDao.findByUserId(BUDGET.getUserId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> budgetService.isBudgetExceeded(BUDGET.getUserId()));
    }
}
