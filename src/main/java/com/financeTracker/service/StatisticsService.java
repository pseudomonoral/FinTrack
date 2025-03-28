package com.financeTracker.service;

import java.time.LocalDate;
import java.util.Map;


/**
 * The StatisticsService interface provides methods for calculating and analyzing financial statistics for users.
 * It includes operations for calculating current balance, income, expenses, and analyzing expenses by category.
 */
public interface StatisticsService {

    /**
     * Calculates the current balance for a specific user.
     *
     * @param userId The ID of the user whose current balance is to be calculated.
     * @return double The current balance of the user.
     */
    double calculateCurrentBalance(Long userId);

    /**
     * Calculates the total income for a specific user within a given period.
     *
     * @param userId The ID of the user whose income is to be calculated.
     * @param startDate The start date of the period.
     * @param endDate The end date of the period.
     * @return double The total income for the user during the specified period.
     */
    double calculateIncomeForPeriod(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * Calculates the total expenses for a specific user within a given period.
     *
     * @param userId The ID of the user whose expenses are to be calculated.
     * @param startDate The start date of the period.
     * @param endDate The end date of the period.
     * @return double The total expenses for the user during the specified period.
     */
    double calculateExpenseForPeriod(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * Analyzes the user's expenses by category and returns the breakdown.
     *
     * @param userId The ID of the user whose expenses are to be analyzed.
     * @return Map<String, Double> A map where keys are expense categories and values are the corresponding amounts.
     */
    Map<String, Double> analyzeExpensesByCategory(Long userId);

    long calculateCountOfCompletedGoal(Long userId);

    double calculateAverageGoalProgress(Long userId);
}

