package com.financeTracker.service.impl;

import com.financeTracker.dao.TransactionDao;
import com.financeTracker.dao.impl.GoalDaoImpl;
import com.financeTracker.dto.statistic.BalanceResponseDto;
import com.financeTracker.dto.statistic.ExpenseAnalysisResponseDto;
import com.financeTracker.dto.statistic.GoalStatisticsResponseDto;
import com.financeTracker.dto.statistic.IncomeExpenseResponseDto;
import com.financeTracker.dto.transaction.TransactionResponseDto;
import com.financeTracker.mapper.StatisticsMapper;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.service.StatisticsService;
import com.financeTracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

import static com.financeTracker.model.type.TransactionType.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final TransactionService transactionService;
    private final TransactionDao transactionDao;
    private final StatisticsMapper statisticsMapper;
    private final GoalDaoImpl goalDaoImpl;

    @Override
    public BalanceResponseDto calculateCurrentBalance(Long userId) {
        double income = transactionDao.findAllByUserId(userId).stream()
                .filter(transaction -> transaction.getType() == INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
        double expense = transactionDao.findAllByUserId(userId).stream()
                .filter(transaction -> transaction.getType() == EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
        return statisticsMapper.toBalanceResponseDto(income - expense);
    }

    @Override
    public IncomeExpenseResponseDto calculateIncomeForPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        double income = transactionService.getTransactionsByDate(userId, startDate.atStartOfDay()).stream()
                .filter(transaction -> isWithinPeriod(transaction, startDate, endDate))
                .filter(transaction -> transaction.getType() == INCOME)
                .mapToDouble(TransactionResponseDto::getAmount)
                .sum();
        return statisticsMapper.toIncomeExpenseResponseDto(income);
    }

    @Override
    public IncomeExpenseResponseDto calculateExpenseForPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        double expense = transactionService.getTransactionsByDate(userId, startDate.atStartOfDay()).stream()
                .filter(transaction -> isWithinPeriod(transaction, startDate, endDate))
                .filter(transaction -> transaction.getType() == EXPENSE)
                .mapToDouble(TransactionResponseDto::getAmount)
                .sum();
        return statisticsMapper.toIncomeExpenseResponseDto(expense);
    }

    @Override
    public ExpenseAnalysisResponseDto analyzeExpensesByCategory(Long userId) {
        Map<String, Double> analyzeExpenses = transactionDao.findAllByUserId(userId).stream()
                .filter(transaction -> transaction.getType() == EXPENSE)
                .collect(Collectors.groupingBy(Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)));
        return statisticsMapper.toExpenseAnalysisResponseDto(analyzeExpenses);
    }

    @Override
    public GoalStatisticsResponseDto analyzeGoalsByUserId(Long userId){
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

    private boolean isWithinPeriod(TransactionResponseDto transaction, LocalDate startDate, LocalDate endDate) {
        LocalDate transactionDate = transaction.getCreatedAt().toLocalDate();
        return !transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate);
    }

    private double getGoalProgress(Goal goal) {
        return goal.getTargetAmount() == 0 ? 100.0 :
                Math.min((goal.getCurrentAmount() / goal.getTargetAmount()) * 100, 100.0);
    }
}