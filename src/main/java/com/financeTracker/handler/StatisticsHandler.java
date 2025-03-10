package com.financeTracker.handler;

import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.StatisticsController;
import com.financeTracker.in.Input;
import com.financeTracker.out.OutputData;

import java.time.LocalDate;
import java.util.Map;

public class StatisticsHandler {

    public static void handleCurrentBalance(Input inputData, OutputData outputData, StatisticsController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        double balance = controller.calculateCurrentBalance(userId);
        outputData.output("Текущий баланс: " + balance);
    }

    public static void handleIncomeForPeriod(Input inputData, OutputData outputData, StatisticsController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        LocalDate startDate = askDate(inputData, outputData, "Введите дату начала (ГГГГ-ММ-ДД):");
        LocalDate endDate = askDate(inputData, outputData, "Введите дату окончания (ГГГГ-ММ-ДД):");
        double income = controller.calculateIncomeForPeriod(userId, startDate, endDate);
        outputData.output("Суммарный доход за период: " + income);
    }

    public static void handleExpenseForPeriod(Input inputData, OutputData outputData, StatisticsController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        LocalDate startDate = askDate(inputData, outputData, "Введите дату начала (ГГГГ-ММ-ДД):");
        LocalDate endDate = askDate(inputData, outputData, "Введите дату окончания (ГГГГ-ММ-ДД):");
        double expense = controller.calculateExpenseForPeriod(userId, startDate, endDate);
        outputData.output("Суммарный расход за период: " + expense);
    }

    public static void handleExpenseAnalysis(Input inputData, OutputData outputData, StatisticsController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        Map<String, Double> expenseAnalysis = controller.analyzeExpensesByCategory(userId);
        outputData.output("Анализ расходов по категориям:");
        expenseAnalysis.forEach((category, amount) -> outputData.output(category + ": " + amount));
    }

    private static LocalDate askDate(Input inputData, OutputData outputData, String message) {
        outputData.output(message);
        String dateInput = inputData.in().toString();
        return LocalDate.parse(dateInput);
    }
}