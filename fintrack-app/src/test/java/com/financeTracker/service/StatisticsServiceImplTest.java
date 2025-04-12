package com.financeTracker.service;

import com.financeTracker.dao.TransactionDao;
import com.financeTracker.dao.impl.GoalDaoImpl;
import com.financeTracker.dto.statistic.BalanceResponseDto;
import com.financeTracker.dto.statistic.ExpenseAnalysisResponseDto;
import com.financeTracker.dto.statistic.IncomeExpenseResponseDto;
import com.financeTracker.mapper.StatisticsMapper;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.service.impl.StatisticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.financeTracker.model.type.TransactionType.EXPENSE;
import static com.financeTracker.model.type.TransactionType.INCOME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class StatisticsServiceImplTest {

    private TransactionDao transactionDao;
    private StatisticsMapper statisticsMapper;
    private GoalDaoImpl goalDao;

    private StatisticsServiceImpl statisticsService;

    @BeforeEach
    void setUp() {
        transactionDao = mock(TransactionDao.class);
        statisticsMapper = mock(StatisticsMapper.class);
        goalDao = mock(GoalDaoImpl.class);
        statisticsService = new StatisticsServiceImpl(transactionDao, statisticsMapper, goalDao);
    }

    @Test
    @DisplayName("calculateCurrentBalance should return correct balance")
    void calculateCurrentBalance_shouldReturnCorrectBalance() {
        Long userId = 1L;
        List<Transaction> incomes = List.of(
                Transaction.builder().amount(100.0).build(),
                Transaction.builder().amount(200.0).build()
        );
        List<Transaction> expenses = List.of(
                Transaction.builder().amount(50.0).build()
        );
        when(transactionDao.findAllByUserIdAndType(userId, INCOME)).thenReturn(incomes);
        when(transactionDao.findAllByUserIdAndType(userId, EXPENSE)).thenReturn(expenses);

        BalanceResponseDto expected = BalanceResponseDto.builder().balance(250.0).build();
        when(statisticsMapper.toBalanceResponseDto(250.0)).thenReturn(expected);

        BalanceResponseDto result = statisticsService.calculateCurrentBalance(userId);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("calculateIncomeForPeriod should return total income mapped to dto")
    void calculateIncomeForPeriod_shouldReturnIncome() {
        Long userId = 1L;
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);
        double total = 300.0;

        when(transactionDao.calculateTotalForPeriod(
                eq(userId),
                eq(INCOME),
                eq(start.atStartOfDay()),
                eq(end.atTime(LocalTime.MAX))
        )).thenReturn(total);

        IncomeExpenseResponseDto expected = IncomeExpenseResponseDto.builder().amount(total).build();
        when(statisticsMapper.toIncomeExpenseResponseDto(total)).thenReturn(expected);

        IncomeExpenseResponseDto result = statisticsService.calculateIncomeForPeriod(userId, start, end);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("calculateExpenseForPeriod should return total expense mapped to dto")
    void calculateExpenseForPeriod_shouldReturnExpense() {
        Long userId = 1L;
        LocalDate start = LocalDate.of(2024, 2, 1);
        LocalDate end = LocalDate.of(2024, 2, 28);
        double total = 150.0;

        when(transactionDao.calculateTotalForPeriod(
                eq(userId),
                eq(EXPENSE),
                eq(start.atStartOfDay()),
                eq(end.atTime(LocalTime.MAX))
        )).thenReturn(total);

        IncomeExpenseResponseDto expected = IncomeExpenseResponseDto.builder().amount(total).build();
        when(statisticsMapper.toIncomeExpenseResponseDto(total)).thenReturn(expected);

        IncomeExpenseResponseDto result = statisticsService.calculateExpenseForPeriod(userId, start, end);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("analyzeExpensesByCategory should return grouped expenses mapped to dto")
    void analyzeExpensesByCategory_shouldReturnGroupedExpenses() {
        Long userId = 1L;
        List<Transaction> transactions = List.of(
                Transaction.builder().category("Food").amount(100.0).build(),
                Transaction.builder().category("Food").amount(50.0).build(),
                Transaction.builder().category("Transport").amount(30.0).build()
        );

        when(transactionDao.findAllByUserIdAndType(userId, EXPENSE)).thenReturn(transactions);

        Map<String, Double> grouped = Map.of(
                "Food", 150.0,
                "Transport", 30.0
        );
        ExpenseAnalysisResponseDto expected = ExpenseAnalysisResponseDto.builder()
                .categoryExpenses(grouped)
                .build();

        when(statisticsMapper.toExpenseAnalysisResponseDto(grouped)).thenReturn(expected);

        ExpenseAnalysisResponseDto result = statisticsService.analyzeExpensesByCategory(userId);

        assertThat(result).isEqualTo(expected);
    }
}

