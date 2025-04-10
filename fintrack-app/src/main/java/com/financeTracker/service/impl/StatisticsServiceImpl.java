package com.financeTracker.service.impl;

import com.financeTracker.dao.TransactionDao;
import com.financeTracker.dao.impl.GoalDaoImpl;
import com.financeTracker.dto.statistic.BalanceResponseDto;
import com.financeTracker.dto.statistic.ExpenseAnalysisResponseDto;
import com.financeTracker.dto.statistic.GoalStatisticsResponseDto;
import com.financeTracker.dto.statistic.IncomeExpenseResponseDto;
import com.financeTracker.mapper.StatisticsMapper;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.stream.Collectors;

import static com.financeTracker.model.type.TransactionType.EXPENSE;
import static com.financeTracker.model.type.TransactionType.INCOME;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final TransactionDao transactionDao;
    private final StatisticsMapper statisticsMapper;
    private final GoalDaoImpl goalDaoImpl;

    @Override
    public BalanceResponseDto calculateCurrentBalance(Long userId) {
        double income = transactionDao.findAllByUserIdAndType(userId, INCOME).stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
        double expense = transactionDao.findAllByUserIdAndType(userId, EXPENSE).stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
        return statisticsMapper.toBalanceResponseDto(income - expense);
    }

    @Override
    public IncomeExpenseResponseDto calculateIncomeForPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        double income = transactionDao.calculateTotalForPeriod(userId, INCOME, startDate.atStartOfDay(),
                endDate.atTime(LocalTime.MAX));
        return statisticsMapper.toIncomeExpenseResponseDto(income);
    }

    @Override
    public IncomeExpenseResponseDto calculateExpenseForPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        double expense = transactionDao.calculateTotalForPeriod(userId, EXPENSE, startDate.atStartOfDay(),
                endDate.atTime(LocalTime.MAX));
        return statisticsMapper.toIncomeExpenseResponseDto(expense);
    }

    @Override
    public ExpenseAnalysisResponseDto analyzeExpensesByCategory(Long userId) {
        Map<String, Double> analyzeExpenses = transactionDao.findAllByUserIdAndType(userId, EXPENSE).stream()
                .collect(Collectors.groupingBy(Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)));
        return statisticsMapper.toExpenseAnalysisResponseDto(analyzeExpenses);
    }

    @Override
    public GoalStatisticsResponseDto analyzeGoalsByUserId(Long userId) {
        return statisticsMapper.toGoalStatisticsResponseDto(calculateCountOfCompletedGoal(userId),
                calculateAverageGoalProgress(userId));
    }

    private long calculateCountOfCompletedGoal(Long userId) {
        return goalDaoImpl.findAllByUserId(userId).stream()
                .filter(Goal::isGoalCompleted)
                .count();
    }

    private double calculateAverageGoalProgress(Long userId) {
        return goalDaoImpl.findAllByUserId(userId).stream()
                .mapToDouble(this::getGoalProgress)
                .average()
                .orElse(0.0);
    }

    private double getGoalProgress(Goal goal) {
        return goal.getTargetAmount() == 0 ? 100.0 :
                Math.min((goal.getCurrentAmount() / goal.getTargetAmount()) * 100, 100.0);
    }
}