package com.financeTracker.service;

import com.financeTracker.dao.BudgetDao;
import com.financeTracker.dao.TransactionDao;
import com.financeTracker.dto.budget.BudgetCheckResponseDto;
import com.financeTracker.dto.budget.BudgetDto;
import com.financeTracker.mapper.BudgetMapper;
import com.financeTracker.model.entity.Budget;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.service.impl.BudgetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.financeTracker.model.type.TransactionType.EXPENSE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class BudgetServiceImplTest {

    private BudgetDao budgetDao;
    private TransactionDao transactionDao;
    private BudgetMapper budgetMapper;

    private BudgetServiceImpl budgetService;

    @BeforeEach
    void setUp() {
        budgetDao = mock(BudgetDao.class);
        transactionDao = mock(TransactionDao.class);
        budgetMapper = mock(BudgetMapper.class);
        budgetService = new BudgetServiceImpl(budgetDao, transactionDao, budgetMapper);
    }

    @Test
    @DisplayName("createBudget should save new budget")
    void createBudget_shouldSaveBudget() {
        Long userId = 1L;
        BudgetDto budgetDto = BudgetDto.builder().limit(500.0).build();

        budgetService.createBudget(userId, budgetDto);

        verify(budgetDao).save(argThat(budget ->
                budget.getUserId().equals(userId) &&
                        budget.getLimit() == 500.0));
    }

    @Test
    @DisplayName("getBudget should return mapped BudgetDto")
    void getBudget_shouldReturnMappedDto() {
        Long userId = 1L;
        Budget budget = Budget.builder().userId(userId).limit(300.0).build();
        BudgetDto dto = BudgetDto.builder().limit(300.0).build();

        when(budgetDao.findByUserId(userId)).thenReturn(Optional.of(budget));
        when(budgetMapper.toBudgetDto(budget)).thenReturn(dto);

        Optional<BudgetDto> result = budgetService.getBudget(userId);

        assertThat(result).contains(dto);
    }

    @Test
    @DisplayName("updateBudget should update existing budget")
    void updateBudget_shouldUpdateExistingBudget() {
        Long userId = 1L;
        BudgetDto budgetDto = BudgetDto.builder().limit(1000.0).build();
        Budget oldBudget = Budget.builder().id(5L).userId(userId).limit(500.0).build();

        when(budgetDao.findByUserId(userId)).thenReturn(Optional.of(oldBudget));

        budgetService.updateBudget(userId, budgetDto);

        verify(budgetDao).updateBudget(argThat(updated ->
                updated.getId().equals(5L) &&
                        updated.getUserId().equals(userId) &&
                        updated.getLimit() == 1000.0
        ));
    }

    @Test
    @DisplayName("deleteBudget should delete existing budget")
    void deleteBudget_shouldDelete() {
        Long userId = 1L;
        Budget budget = Budget.builder().id(10L).userId(userId).limit(400.0).build();

        when(budgetDao.findByUserId(userId)).thenReturn(Optional.of(budget));

        budgetService.deleteBudget(userId);

        verify(budgetDao).delete(10L);
    }

    @Test
    @DisplayName("isBudgetExceeded should return exceeded = true when expense > limit")
    void isBudgetExceeded_shouldReturnExceededTrue() {
        Long userId = 1L;
        Budget budget = Budget.builder().userId(userId).limit(100.0).build();
        Transaction recentExpense = Transaction.builder()
                .userId(userId)
                .amount(150.0)
                .type(EXPENSE)
                .createdAt(LocalDateTime.now())
                .build();

        when(budgetDao.findByUserId(userId)).thenReturn(Optional.of(budget));
        when(transactionDao.findAll()).thenReturn(List.of(recentExpense));

        BudgetCheckResponseDto result = budgetService.isBudgetExceeded(userId);

        assertThat(result.isExceeded()).isTrue();
        assertThat(result.getMonthlyExpense()).isEqualTo(150.0);
        assertThat(result.getBudgetLimit()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("isBudgetExceeded should return exceeded = false when expense <= limit")
    void isBudgetExceeded_shouldReturnExceededFalse() {
        Long userId = 1L;
        Budget budget = Budget.builder().userId(userId).limit(200.0).build();
        Transaction recentExpense = Transaction.builder()
                .userId(userId)
                .amount(50.0)
                .type(EXPENSE)
                .createdAt(LocalDateTime.now())
                .build();

        when(budgetDao.findByUserId(userId)).thenReturn(Optional.of(budget));
        when(transactionDao.findAll()).thenReturn(List.of(recentExpense));

        BudgetCheckResponseDto result = budgetService.isBudgetExceeded(userId);

        assertThat(result.isExceeded()).isFalse();
        assertThat(result.getMonthlyExpense()).isEqualTo(50.0);
        assertThat(result.getBudgetLimit()).isEqualTo(200.0);
    }
}
