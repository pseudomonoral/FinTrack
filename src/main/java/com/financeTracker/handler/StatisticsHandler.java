package com.financeTracker.handler;

import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.StatisticsController;
import com.financeTracker.in.Input;
import com.financeTracker.model.type.UserLocation;
import com.financeTracker.out.OutputData;

import java.time.LocalDate;
import java.util.Map;

/**
 * Allows querying and displaying data about the current balance, income, expenses, and expense analysis by categories.
 */
public class StatisticsHandler {

    /**
     * Handles the request to calculate the user's current balance.
     * @param inputData input data
     * @param outputData output data
     * @param controller statistics controller
     * @return the current location of the user in the application
     */
    public static UserLocation handleCurrentBalance(Input inputData, OutputData outputData, StatisticsController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        double balance = controller.calculateCurrentBalance(userId);
        outputData.output("Текущий баланс: " + balance);
        return UserLocation.MAIN_MENU_LOC;
    }

    /**
     * Handles the request to calculate the income for a given period.
     * @param inputData input data
     * @param outputData output data
     * @param controller statistics controller
     * @return the current location of the user in the application
     */
    public static UserLocation handleIncomeForPeriod(Input inputData, OutputData outputData, StatisticsController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        LocalDate startDate = askDate(inputData, outputData, "Введите дату начала (ГГГГ-ММ-ДД):");
        LocalDate endDate = askDate(inputData, outputData, "Введите дату окончания (ГГГГ-ММ-ДД):");
        double income = controller.calculateIncomeForPeriod(userId, startDate, endDate);
        outputData.output("Суммарный доход за период: " + income);
        return UserLocation.MAIN_MENU_LOC;
    }

    /**
     * Handles the request to calculate the expenses for a given period.
     * @param inputData input data
     * @param outputData output data
     * @param controller statistics controller
     * @return the current location of the user in the application
     */
    public static UserLocation handleExpenseForPeriod(Input inputData, OutputData outputData, StatisticsController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        LocalDate startDate = askDate(inputData, outputData, "Введите дату начала (ГГГГ-ММ-ДД):");
        LocalDate endDate = askDate(inputData, outputData, "Введите дату окончания (ГГГГ-ММ-ДД):");
        double expense = controller.calculateExpenseForPeriod(userId, startDate, endDate);
        outputData.output("Суммарный расход за период: " + expense);
        return UserLocation.MAIN_MENU_LOC;
    }

    /**
     * Handles the request for expense analysis by category.
     * @param inputData input data
     * @param outputData output data
     * @param controller statistics controller
     * @return the current location of the user in the application
     */
    public static UserLocation handleExpenseAnalysis(Input inputData, OutputData outputData, StatisticsController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        Map<String, Double> expenseAnalysis = controller.analyzeExpensesByCategory(userId);
        outputData.output("Анализ расходов по категориям:");
        expenseAnalysis.forEach((category, amount) -> outputData.output(category + ": " + amount));
        return UserLocation.MAIN_MENU_LOC;
    }

    /**
     * Asks the user for a date in the format YYYY-MM-DD.
     * @param inputData input data
     * @param outputData output data
     * @param message message for the user
     * @return the date entered by the user
     */
    private static LocalDate askDate(Input inputData, OutputData outputData, String message) {
        outputData.output(message);
        String dateInput = inputData.in().toString();
        return LocalDate.parse(dateInput);
    }
}
