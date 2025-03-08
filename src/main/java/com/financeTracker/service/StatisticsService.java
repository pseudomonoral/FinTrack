package com.financeTracker.service;

import java.time.LocalDate;
import java.util.Map;

/**
 * - Подсчёт текущего баланса.
 *
 * - Расчёт суммарного дохода и расхода за определённый период.
 *
 * - Анализ расходов по категориям.
 *
 * - Формирование отчёта для пользователя по финансовому состоянию.
 */
public interface StatisticsService {

    double calculateCurrentBalance();

    double calculateMonthlySummary();

    double calculateSummaryForPeriod(LocalDate startDate,
                                     LocalDate endDate);

    Map<String, Double> analyzeExpensesByCategory();

    String generateFinancialReport();
}
