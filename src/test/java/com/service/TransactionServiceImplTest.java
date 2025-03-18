package com.service;

import com.financeTracker.dao.TransactionDao;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.service.BudgetService;
import com.financeTracker.service.GoalService;
import com.financeTracker.service.impl.TransactionServiceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
    @DisplayName("Delete Transaction - Should Delete When Exists")
    void deleteTransaction_ShouldDelete_WhenExists() {
        when(transactionDao.findById(TRANSACTION.getId())).thenReturn(Optional.of(TRANSACTION));

        transactionService.deleteTransaction(TRANSACTION.getId());

        verify(transactionDao, times(1)).delete(TRANSACTION.getId());
    }

    @Test
    @DisplayName("Delete Transaction - Should Throw Exception When Not Found")
    void deleteTransaction_ShouldThrowException_WhenNotFound() {
        when(transactionDao.findById(TRANSACTION.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> transactionService.deleteTransaction(TRANSACTION.getId()));
    }
}
