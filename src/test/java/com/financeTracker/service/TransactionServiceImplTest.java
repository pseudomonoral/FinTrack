package ru.polskiy.service;


import com.financeTracker.dao.TransactionDao;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.service.BudgetService;
import com.financeTracker.service.GoalService;
import com.financeTracker.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.polskiy.constants.TestConstants.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionDao transactionDao;

    @Mock
    private BudgetService budgetService;

    @Mock
    private GoalService goalService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    @DisplayName("Create Transaction - Should Save Transaction When Valid")
    void createTransaction_ShouldSaveTransaction_WhenValid() {
        when(budgetService.getBudget(USER.getId())).thenReturn(BUDGET);
        when(goalService.getGoal(GOAL.getUserId())).thenReturn(GOAL);
        when(transactionDao.save(TRANSACTION)).thenReturn(TRANSACTION);

        Transaction result = transactionService.createTransaction(TRANSACTION.getAmount(), TRANSACTION.getCategory(),
                TRANSACTION.getDescription(), TransactionType.INCOME);

        assertNotNull(result);
        assertThat(TRANSACTION.getAmount()).isEqualTo(result.getAmount());
        verify(transactionDao, times(1)).save(TRANSACTION);
    }

    @Test
    @DisplayName("Create Transaction - Should Throw Exception When Budget Exceeded")
    void createTransaction_ShouldThrowException_WhenBudgetExceeded() {
        BUDGET.setLimit(50.0);
        when(budgetService.getBudget(USER.getId())).thenReturn(BUDGET);
        when(goalService.getGoal(GOAL.getUserId())).thenReturn(GOAL);

        assertThrows(ArithmeticException.class, () -> transactionService.createTransaction(TRANSACTION.getAmount(),
                TRANSACTION.getCategory(),
                TRANSACTION.getDescription(), TransactionType.EXPENSE));
    }

    @Test
    @DisplayName("Delete Transaction - Should Delete When Exists")
    void deleteTransaction_ShouldDelete_WhenExists() {
        when(transactionDao.findById(TRANSACTION.getId())).thenReturn(Optional.of(TRANSACTION));

        transactionService.deleteTransaction(TRANSACTION.getId());

        verify(transactionDao, times(1)).delete(TRANSACTION);
    }

    @Test
    @DisplayName("Delete Transaction - Should Throw Exception When Not Found")
    void deleteTransaction_ShouldThrowException_WhenNotFound() {
        when(transactionDao.findById(TRANSACTION.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> transactionService.deleteTransaction(TRANSACTION.getId()));
    }

    @Test
    @DisplayName("Update Transaction - Should Update When Exists")
    void updateTransaction_ShouldUpdate_WhenExists() {
        when(transactionDao.findById(TRANSACTION.getId())).thenReturn(Optional.of(TRANSACTION));

        transactionService.updateTransaction(TRANSACTION);

        verify(transactionDao, times(1)).updateTransaction(TRANSACTION);
    }

    @Test
    @DisplayName("Update Transaction - Should Throw Exception When Not Found")
    void updateTransaction_ShouldThrowException_WhenNotFound() {
        when(transactionDao.findById(TRANSACTION.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> transactionService.updateTransaction(TRANSACTION));
    }
}
