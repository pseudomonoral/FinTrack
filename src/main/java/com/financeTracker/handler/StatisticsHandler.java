package com.financeTracker.handler;

import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.BudgetController;
import com.financeTracker.controller.GoalController;
import com.financeTracker.controller.StatisticsController;
import com.financeTracker.controller.UserController;
import com.financeTracker.in.Input;
import com.financeTracker.model.entity.Budget;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.model.type.UserLocation;
import com.financeTracker.out.OutputData;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static com.financeTracker.model.type.UserLocation.STATISTICS_MENU_LOC;

/**
 * Allows querying and displaying data about the current balance, income, expenses, and expense analysis by categories.
 */
public class StatisticsHandler {

    /**
     * Handles the request to calculate the user's current balance.
     * @param outputData output data
     * @param controller statistics controller
     * @return the current location of the user in the application
     */
    public static UserLocation handleCurrentBalance(OutputData outputData, StatisticsController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        double balance = controller.calculateCurrentBalance(userId);
        outputData.output("Текущий баланс: " + balance);
        return STATISTICS_MENU_LOC;
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
        return STATISTICS_MENU_LOC;
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
        return STATISTICS_MENU_LOC;
    }

    /**
     * Handles the request for expense analysis by category.
     * @param outputData output data
     * @param controller statistics controller
     * @return the current location of the user in the application
     */
    public static UserLocation handleExpenseAnalysis(OutputData outputData, StatisticsController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        Map<String, Double> expenseAnalysis = controller.analyzeExpensesByCategory(userId);
        outputData.output("Анализ расходов по категориям:");
        expenseAnalysis.forEach((category, amount) -> outputData.output(category + ": " + amount));
        return STATISTICS_MENU_LOC;
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

    public static UserLocation generateReport(OutputData outputData, StatisticsController statisticsController,
                                      BudgetController budgetController, UserController userController,
                                      GoalController goalController) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();

        double currentBalance = statisticsController.calculateCurrentBalance(userId);

        String budgetState;
        Optional<Budget> budget = budgetController.getBudget(userId);
        if (budget.isPresent()) {
            budgetState = budgetController.isBudgetExceeded(userId) ?
                    "превышен" : "в норме";
        } else {
            budgetState = "не установлен";
        }

        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);
        double monthlyExpenses = statisticsController.calculateExpenseForPeriod(userId, monthStart, today);
        double monthlyIncome = statisticsController.calculateIncomeForPeriod(userId, monthStart, today);

        LocalDate dateOfUserRegistration = userController.getUserById(userId).getCreatedAt().toLocalDate();

        double totalExpenses = statisticsController.calculateExpenseForPeriod(userId,
                dateOfUserRegistration, today);
        double totalIncome = statisticsController.calculateExpenseForPeriod(userId,
                dateOfUserRegistration, today);

        String goalState;
        Optional<Goal> actualGoal = goalController.getActualGoalByUserId(userId);
        goalState = actualGoal.map(goal -> String.format("выполнена на ", goalController.getGoalProgress(goal)))
                .orElse("не установлена");

        Long countCompletedGoal = statisticsController.calculateCountOfCompletedGoal(userId);
        Double averageGoalProgress = statisticsController.calculateAverageGoalProgress(userId);

        String report = String.format("""
            Текущий баланс: %.2f
            
            Месячный бюджет: %s
            
            Текущая цель: %s
            
            Расходы за месяц: %.2f
            Доходы за месяц: %.2f

            Общая сумма расходов: %.2f
            Общая сумма доходов: %.2f
            
            Количество выполненных целей: %d
            Средний процент выполнения целей: %.2f%%
            """,
                currentBalance, goalState, budgetState,
                monthlyExpenses, monthlyIncome,
                totalExpenses, totalIncome,
                countCompletedGoal, averageGoalProgress);

        outputData.output(report);

        return STATISTICS_MENU_LOC;
    }
}
