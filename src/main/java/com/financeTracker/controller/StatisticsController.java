package com.financeTracker.controller;

import com.financeTracker.dto.BalanceResponseDto;
import com.financeTracker.dto.ExpenseAnalysisResponseDto;
import com.financeTracker.dto.GoalStatisticsResponseDto;
import com.financeTracker.dto.IncomeExpenseResponseDto;
import com.financeTracker.mapper.StatisticsMapper;
import com.financeTracker.service.StatisticsService;

import java.time.LocalDate;
import java.util.Map;

public class StatisticsController {
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    public BalanceResponseDto getCurrentBalance(Long userId) {
        double balance = statisticsService.calculateCurrentBalance(userId);
        return StatisticsMapper.INSTANCE.toBalanceResponseDto(balance);
    }

    public IncomeExpenseResponseDto getIncomeForPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        double income = statisticsService.calculateIncomeForPeriod(userId, startDate, endDate);
        return StatisticsMapper.INSTANCE.toIncomeExpenseResponseDto(income);
    }

    public IncomeExpenseResponseDto getExpenseForPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        double expense = statisticsService.calculateExpenseForPeriod(userId, startDate, endDate);
        return StatisticsMapper.INSTANCE.toIncomeExpenseResponseDto(expense);
    }

    public ExpenseAnalysisResponseDto getExpenseAnalysis(Long userId) {
        Map<String, Double> analysis = statisticsService.analyzeExpensesByCategory(userId);
        return StatisticsMapper.INSTANCE.toExpenseAnalysisResponseDto(analysis);
    }

    public GoalStatisticsResponseDto getGoalStatistics(Long userId) {
        Long completedGoals = statisticsService.calculateCountOfCompletedGoal(userId);
        Double averageGoalProgress = statisticsService.calculateAverageGoalProgress(userId);
        return StatisticsMapper.INSTANCE.toGoalStatisticsResponseDto(completedGoals, averageGoalProgress);
    }
}

