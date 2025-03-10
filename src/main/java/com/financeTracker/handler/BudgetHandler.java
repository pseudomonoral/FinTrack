package com.financeTracker.handler;

import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.BudgetController;
import com.financeTracker.controller.StatisticsController;
import com.financeTracker.in.Input;
import com.financeTracker.model.entity.Budget;
import com.financeTracker.model.type.UserLocation;
import com.financeTracker.out.OutputData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class BudgetHandler {

    /**
     * Handles creating a budget for the user.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @param controller The budget controller.
     * @return The next user location after budget creation.
     */
    public static UserLocation handleCreateBudget(Input inputData, OutputData outputData, BudgetController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();

        outputData.output("Введите лимит бюджета:");
        double limit = Double.parseDouble(inputData.in().toString());

        controller.createBudget(userId, limit);
        outputData.output("Бюджет успешно создан с лимитом: " + limit);

        return UserLocation.BUDGET_MENU_LOC;
    }

    /**
     * Handles retrieving the user's budget.
     *
     * @param outputData The output data provider.
     * @param controller The budget controller.
     * @return The next user location after viewing the budget.
     */
    public static UserLocation handleViewBudget(OutputData outputData, BudgetController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        Optional<Budget> budget = controller.getBudget(userId);

        if (budget.isPresent()) {
            outputData.output("Ваш бюджет: " + formatBudget(budget.get()));
        } else {
            outputData.output("Бюджет не установлен.");
        }

        return UserLocation.BUDGET_MENU_LOC;
    }

    /**
     * Handles updating an existing budget.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @param controller The budget controller.
     * @return The next user location after updating the budget.
     */
    public static UserLocation handleUpdateBudget(Input inputData, OutputData outputData, BudgetController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();

        outputData.output("Введите новый лимит бюджета:");
        double newLimit = Double.parseDouble(inputData.in().toString());

        controller.updateBudget(userId, newLimit);
        outputData.output("Бюджет успешно обновлен. Новый лимит: " + newLimit);

        return UserLocation.BUDGET_MENU_LOC;
    }

    /**
     * Handles deleting the user's budget.
     *
     * @param outputData The output data provider.
     * @param controller The budget controller.
     * @return The next user location after budget deletion.
     */
    public static UserLocation handleDeleteBudget(OutputData outputData, BudgetController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();

        controller.deleteBudget(userId);
        outputData.output("Ваш бюджет был удален.");

        return UserLocation.BUDGET_MENU_LOC;
    }

    /**
     * Handles checking if the user's budget is exceeded.
     *
     * @param outputData The output data provider.
     * @param budgetController The budget controller.
     * @return The next user location after checking budget status.
     */
    public static UserLocation handleCheckBudgetExceeded(OutputData outputData,
                                                         BudgetController budgetController,
                                                         StatisticsController statisticsController) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();

        boolean exceeded = budgetController.isBudgetExceeded(userId);
        LocalDate oneMonthAgo = LocalDate.from(LocalDateTime.now().minusMonths(1));
        double expense = statisticsController.calculateExpenseForPeriod(userId,
                oneMonthAgo, LocalDate.now());
        Budget budget = budgetController.getBudget(userId).get();

        if (exceeded) {
            outputData.output("Вы превысили свой бюджет!");
        } else {
            outputData.output("Ваши расходы находятся в пределах бюджета.");
        }

        String actualInfo = "Траты за месяц: " + expense + "\n" + formatBudget(budget);

        outputData.output(actualInfo);

        return UserLocation.BUDGET_MENU_LOC;
    }

    /**
     * Formats a budget for display.
     *
     * @param budget The budget to format.
     * @return A formatted string representation of the budget.
     */
    private static String formatBudget(Budget budget) {
        return String.format("Лимит: %.2f", budget.getLimit());
    }
}
