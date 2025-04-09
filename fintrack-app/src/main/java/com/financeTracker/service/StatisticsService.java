package com.financeTracker.service;

import com.financeTracker.dto.statistic.BalanceResponseDto;
import com.financeTracker.dto.statistic.ExpenseAnalysisResponseDto;
import com.financeTracker.dto.statistic.GoalStatisticsResponseDto;
import com.financeTracker.dto.statistic.IncomeExpenseResponseDto;

import java.time.LocalDate;


/**
 * Service interface for calculating financial statistics and analyzing goals.
 */
public interface StatisticsService {

    /**
     * Calculates the current balance for a user by subtracting total expenses from total income.
     *
     * @param userId the ID of the user.
     * @return {@link BalanceResponseDto} containing the current balance.
     */
    BalanceResponseDto calculateCurrentBalance(Long userId);

    /**
     * Calculates total income for a user within a specified date range.
     *
     * @param userId    the ID of the user.
     * @param startDate the start date of the period (inclusive).
     * @param endDate   the end date of the period (inclusive).
     * @return {@link IncomeExpenseResponseDto} containing the total income.
     */
    IncomeExpenseResponseDto calculateIncomeForPeriod(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * Calculates total expenses for a user within a specified date range.
     *
     * @param userId    the ID of the user.
     * @param startDate the start date of the period (inclusive).
     * @param endDate   the end date of the period (inclusive).
     * @return {@link IncomeExpenseResponseDto} containing the total expenses.
     */
    IncomeExpenseResponseDto calculateExpenseForPeriod(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * Analyzes user expenses by category and summarizes them.
     *
     * @param userId the ID of the user.
     * @return {@link ExpenseAnalysisResponseDto} containing categorized expense data.
     */
    ExpenseAnalysisResponseDto analyzeExpensesByCategory(Long userId);

    /**
     * Analyzes user goals, returning goal-related statistics such as count of completed goals and average progress.
     *
     * @param userId the ID of the user.
     * @return {@link GoalStatisticsResponseDto} containing goal analysis results.
     */
    GoalStatisticsResponseDto analyzeGoalsByUserId(Long userId);
}


