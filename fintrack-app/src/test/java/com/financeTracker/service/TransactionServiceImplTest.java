package com.financeTracker.service;

import com.financeTracker.dao.BudgetDao;
import com.financeTracker.dao.GoalDao;
import com.financeTracker.dao.TransactionDao;
import com.financeTracker.dto.TransactionRequestDto;
import com.financeTracker.dto.TransactionResponseDto;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.mapper.TransactionMapper;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionDao transactionDao;
    @Mock
    private GoalDao goalDao;
    @Mock private BudgetDao budgetDao;
    @Mock private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private final Long userId = 1L;
    private final Long transactionId = 10L;
    private final Transaction transaction = Transaction.builder()
            .id(transactionId)
            .userId(userId)
            .amount(100)
            .category("Food")
            .description("Dinner")
            .type(TransactionType.EXPENSE)
            .createdAt(LocalDateTime.now())
            .build();

    private final TransactionResponseDto responseDto = TransactionResponseDto.builder()
            .id(transactionId)
            .amount(100)
            .category("Food")
            .description("Dinner")
            .type(TransactionType.EXPENSE)
            .createdAt(LocalDateTime.now())
            .build();

    @Test
    void createTransaction_shouldReturnResponseDto() {
        TransactionRequestDto request = new TransactionRequestDto(100, "Food", "Dinner", TransactionType.EXPENSE);

        when(transactionDao.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toResponseTransactionDto(transaction)).thenReturn(responseDto);
        when(goalDao.findAllByUserId(userId)).thenReturn(Collections.emptyList());
        when(budgetDao.findByUserId(userId)).thenReturn(Optional.empty());

        TransactionResponseDto result = transactionService.createTransaction(userId, request);

        assertEquals(responseDto, result);
        verify(transactionDao).save(any(Transaction.class));
        verify(transactionMapper).toResponseTransactionDto(transaction);
    }


    @Test
    void deleteTransaction_shouldDeleteAndApply() {
        Transaction existing = Transaction.builder()
                .id(transactionId)
                .userId(userId)
                .amount(200)
                .type(TransactionType.INCOME)
                .build();

        when(transactionDao.findById(transactionId)).thenReturn(Optional.of(existing));
        when(goalDao.findAllByUserId(userId)).thenReturn(Collections.emptyList());
        when(budgetDao.findByUserId(userId)).thenReturn(Optional.empty());

        transactionService.deleteTransaction(transactionId);

        verify(transactionDao).delete(transactionId);
    }


    @Test
    void deleteTransaction_shouldThrowIfNotFound() {
        when(transactionDao.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> transactionService.deleteTransaction(transactionId));
    }

    @Test
    void updateTransaction_shouldUpdateIfFoundAndDifferentAmount() {
        Transaction oldTransaction = Transaction.builder()
                .id(transactionId)
                .userId(userId)
                .amount(100)
                .type(TransactionType.EXPENSE)
                .createdAt(LocalDateTime.now())
                .build();

        TransactionRequestDto updateDto = new TransactionRequestDto(150, "Food", "Lunch", TransactionType.EXPENSE);

        when(transactionDao.findById(transactionId)).thenReturn(Optional.of(oldTransaction));
        when(goalDao.findAllByUserId(userId)).thenReturn(Collections.emptyList());
        when(budgetDao.findByUserId(userId)).thenReturn(Optional.empty());

        transactionService.updateTransaction(userId, transactionId, updateDto);

        verify(transactionDao).updateTransaction(any(Transaction.class));
    }


    @Test
    void updateTransaction_shouldThrowIfNotFound() {
        when(transactionDao.findById(transactionId)).thenReturn(Optional.empty());

        TransactionRequestDto requestDto = new TransactionRequestDto(200, "Food", "Dinner", TransactionType.INCOME);

        assertThrows(EntityNotFoundException.class,
                () -> transactionService.updateTransaction(userId, transactionId, requestDto));
    }

    @Test
    void getTransactionsByUserId_shouldReturnList() {
        when(transactionDao.findAllByUserId(userId)).thenReturn(List.of(transaction));
        when(transactionMapper.toResponseTransactionDto(transaction)).thenReturn(responseDto);

        List<TransactionResponseDto> result = transactionService.getTransactionsByUserId(userId);

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
    }

    @Test
    void getTransactionsByType_shouldFilterCorrectly() {
        when(transactionDao.findAll()).thenReturn(List.of(transaction));
        when(transactionMapper.toResponseTransactionDto(transaction)).thenReturn(responseDto);

        List<TransactionResponseDto> result = transactionService.getTransactionsByType(userId, "EXPENSE");

        assertEquals(1, result.size());
        assertEquals(TransactionType.EXPENSE, result.get(0).getType());
    }
}

