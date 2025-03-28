package com.financeTracker.controller;

import com.financeTracker.service.StatisticsService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * Calculates the current balance of the user.
     *
     * @param userId The user's ID.
     * @return The current balance of the user.
     */
    public double calculateCurrentBalance(Long userId) {
        return statisticsService.calculateCurrentBalance(userId);
    }

    /**
     * Calculates the income of the user for a specific period.
     *
     * @param userId   The user's ID.
     * @param startDate The start date of the period.
     * @param endDate   The end date of the period.
     * @return The income of the user for the period.
     */
    public double calculateIncomeForPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        return statisticsService.calculateIncomeForPeriod(userId, startDate, endDate);
    }

    /**
     * Calculates the expenses of the user for a specific period.
     *
     * @param userId   The user's ID.
     * @param startDate The start date of the period.
     * @param endDate   The end date of the period.
     * @return The expenses of the user for the period.
     */
    public double calculateExpenseForPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        return statisticsService.calculateExpenseForPeriod(userId, startDate, endDate);
    }

    /**
     * Analyzes the user's expenses by category.
     *
     * @param userId The user's ID.
     * @return A map with the categories and their corresponding expenses.
     */
    public Map<String, Double> analyzeExpensesByCategory(Long userId) {
        return statisticsService.analyzeExpensesByCategory(userId);
    }
}
