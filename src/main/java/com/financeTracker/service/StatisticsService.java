package com.financeTracker.service;

import java.time.LocalDate;
import java.util.Map;


public interface StatisticsService {

    double calculateCurrentBalance(Long userId);

    double calculateIncomeForPeriod(Long userId, LocalDate startDate,
                                     LocalDate endDate);

    double calculateExpenseForPeriod(Long userId, LocalDate startDate,
                                    LocalDate endDate);

    Map<String, Double> analyzeExpensesByCategory(Long userId);
}
