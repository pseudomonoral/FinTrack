package com.financeTracker.service;

import com.financeTracker.dao.TransactionDao;
import com.financeTracker.dao.impl.GoalDaoImpl;
import com.financeTracker.dto.*;
import com.financeTracker.mapper.StatisticsMapper;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.service.impl.StatisticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.financeTracker.model.type.TransactionType.EXPENSE;
import static com.financeTracker.model.type.TransactionType.INCOME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class StatisticsServiceImplTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionDao transactionDao;

    @Mock
    private StatisticsMapper statisticsMapper;

    @Mock
    private GoalDaoImpl goalDaoImpl;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calculateCurrentBalance_shouldReturnCorrectBalance() {
        long userId = 1L;

        Transaction income1 = createTransaction(1000, INCOME);
        Transaction expense1 = createTransaction(200, EXPENSE);

        when(transactionDao.findAllByUserId(userId)).thenReturn(List.of(income1, expense1));
        when(statisticsMapper.toBalanceResponseDto(800.0)).thenReturn(new BalanceResponseDto(800));

        BalanceResponseDto response = statisticsService.calculateCurrentBalance(userId);

        assertThat(response.getBalance()).isEqualTo(800);
    }

    @Test
    void calculateIncomeForPeriod_shouldReturnCorrectIncome() {
        long userId = 1L;
        LocalDate start = LocalDate.now().minusDays(10);
        LocalDate end = LocalDate.now();

        TransactionResponseDto t1 = createTransactionDto(500, INCOME, start.atStartOfDay().plusDays(2));
        TransactionResponseDto t2 = createTransactionDto(200, INCOME, start.atStartOfDay().plusDays(5));

        when(transactionService.getTransactionsByDate(eq(userId), any())).thenReturn(List.of(t1, t2));
        when(statisticsMapper.toIncomeExpenseResponseDto(700.0)).thenReturn(new IncomeExpenseResponseDto(700));

        IncomeExpenseResponseDto result = statisticsService.calculateIncomeForPeriod(userId, start, end);

        assertThat(result.getAmount()).isEqualTo(700);
    }

    @Test
    void calculateExpenseForPeriod_shouldReturnCorrectExpense() {
        long userId = 1L;
        LocalDate start = LocalDate.now().minusDays(10);
        LocalDate end = LocalDate.now();

        TransactionResponseDto t1 = createTransactionDto(300, EXPENSE, start.atStartOfDay().plusDays(1));
        TransactionResponseDto t2 = createTransactionDto(100, EXPENSE, start.atStartOfDay().plusDays(2));

        when(transactionService.getTransactionsByDate(eq(userId), any())).thenReturn(List.of(t1, t2));
        when(statisticsMapper.toIncomeExpenseResponseDto(400.0)).thenReturn(new IncomeExpenseResponseDto(400));

        IncomeExpenseResponseDto result = statisticsService.calculateExpenseForPeriod(userId, start, end);

        assertThat(result.getAmount()).isEqualTo(400);
    }

    @Test
    void analyzeExpensesByCategory_shouldReturnCorrectMap() {
        long userId = 1L;

        Transaction t1 = createTransactionWithCategory(300, "Food");
        Transaction t2 = createTransactionWithCategory(100, "Transport");

        when(transactionDao.findAllByUserId(userId)).thenReturn(List.of(t1, t2));
        Map<String, Double> expectedMap = Map.of("Food", 300.0, "Transport", 100.0);
        ExpenseAnalysisResponseDto expectedDto = new ExpenseAnalysisResponseDto(expectedMap);

        when(statisticsMapper.toExpenseAnalysisResponseDto(expectedMap)).thenReturn(expectedDto);

        ExpenseAnalysisResponseDto result = statisticsService.analyzeExpensesByCategory(userId);

        assertThat(result.getCategoryExpenses()).isEqualTo(expectedMap);
    }

    @Test
    void analyzeGoalsByUserId_shouldReturnCorrectStats() {
        long userId = 1L;

        Goal g1 = createGoal(true, 1000, 1000);
        Goal g2 = createGoal(false, 500, 250);

        when(goalDaoImpl.findAllByUserId(userId)).thenReturn(List.of(g1, g2));
        GoalStatisticsResponseDto expected = new GoalStatisticsResponseDto(1L, 75.0);

        when(statisticsMapper.toGoalStatisticsResponseDto(1L, 75.0)).thenReturn(expected);

        GoalStatisticsResponseDto result = statisticsService.analyzeGoalsByUserId(userId);

        assertThat(result.getCompletedGoals()).isEqualTo(1L);
        assertThat(result.getAverageGoalProgress()).isEqualTo(75.0);
    }

    private Transaction createTransaction(double amount, TransactionType type) {
        return Transaction.builder()
                .amount(amount)
                .type(type)
                .build();
    }

    private Transaction createTransactionWithCategory(double amount, String category) {
        return Transaction.builder()
                .amount(amount)
                .category(category)
                .type(EXPENSE)
                .build();
    }

    private TransactionResponseDto createTransactionDto(double amount, TransactionType type, LocalDateTime dateTime) {
        return TransactionResponseDto.builder()
                .amount(amount)
                .type(type)
                .createdAt(dateTime)
                .build();
    }

    private Goal createGoal(boolean completed, double target, double current) {
        return Goal.builder()
                .goalCompleted(completed)
                .targetAmount(target)
                .currentAmount(current)
                .build();
    }
}
