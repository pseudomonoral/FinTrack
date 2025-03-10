package com.financeTracker.handler;

import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.StatisticsController;
import com.financeTracker.in.Input;
import com.financeTracker.model.type.UserLocation;
import com.financeTracker.out.OutputData;

import java.time.LocalDate;
import java.util.Map;

/**
 * Позволяет запрашивать и выводить данные о текущем балансе, доходах, расходах и анализе расходов по категориям.
 */
public class StatisticsHandler {

    /**
     * Обрабатывает запрос на подсчет текущего баланса пользователя.
     * @param inputData входные данные
     * @param outputData выходные данные
     * @param controller контроллер статистики
     * @return текущее местоположение пользователя в приложении
     */
    public static UserLocation handleCurrentBalance(Input inputData, OutputData outputData, StatisticsController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        double balance = controller.calculateCurrentBalance(userId);
        outputData.output("Текущий баланс: " + balance);
        return UserLocation.MAIN_MENU;
    }

    /**
     * Обрабатывает запрос на расчет доходов за заданный период.
     * @param inputData входные данные
     * @param outputData выходные данные
     * @param controller контроллер статистики
     * @return текущее местоположение пользователя в приложении
     */
    public static UserLocation handleIncomeForPeriod(Input inputData, OutputData outputData, StatisticsController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        LocalDate startDate = askDate(inputData, outputData, "Введите дату начала (ГГГГ-ММ-ДД):");
        LocalDate endDate = askDate(inputData, outputData, "Введите дату окончания (ГГГГ-ММ-ДД):");
        double income = controller.calculateIncomeForPeriod(userId, startDate, endDate);
        outputData.output("Суммарный доход за период: " + income);
        return UserLocation.MAIN_MENU;
    }

    /**
     * Обрабатывает запрос на расчет расходов за заданный период.
     * @param inputData входные данные
     * @param outputData выходные данные
     * @param controller контроллер статистики
     * @return текущее местоположение пользователя в приложении
     */
    public static UserLocation handleExpenseForPeriod(Input inputData, OutputData outputData, StatisticsController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        LocalDate startDate = askDate(inputData, outputData, "Введите дату начала (ГГГГ-ММ-ДД):");
        LocalDate endDate = askDate(inputData, outputData, "Введите дату окончания (ГГГГ-ММ-ДД):");
        double expense = controller.calculateExpenseForPeriod(userId, startDate, endDate);
        outputData.output("Суммарный расход за период: " + expense);
        return UserLocation.MAIN_MENU;
    }

    /**
     * Обрабатывает запрос на анализ расходов по категориям.
     * @param inputData входные данные
     * @param outputData выходные данные
     * @param controller контроллер статистики
     * @return текущее местоположение пользователя в приложении
     */
    public static UserLocation handleExpenseAnalysis(Input inputData, OutputData outputData, StatisticsController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        Map<String, Double> expenseAnalysis = controller.analyzeExpensesByCategory(userId);
        outputData.output("Анализ расходов по категориям:");
        expenseAnalysis.forEach((category, amount) -> outputData.output(category + ": " + amount));
        return UserLocation.MAIN_MENU;
    }

    /**
     * Запрашивает у пользователя дату в формате ГГГГ-ММ-ДД.
     * @param inputData входные данные
     * @param outputData выходные данные
     * @param message сообщение для пользователя
     * @return введенная пользователем дата
     */
    private static LocalDate askDate(Input inputData, OutputData outputData, String message) {
        outputData.output(message);
        String dateInput = inputData.in().toString();
        return LocalDate.parse(dateInput);
    }
}
