package com.financeTracker.handler;

import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.GoalController;
import com.financeTracker.controller.StatisticsController;
import com.financeTracker.in.Input;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.model.type.UserLocation;
import com.financeTracker.out.OutputData;

import java.util.List;
import java.util.Optional;

public class GoalHandler {

    /**
     * Handles the creation of a savings goal.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @param goalController The goal controller.
     * @return The next user location after goal creation.
     */
    public static UserLocation handleCreateGoal(Input inputData, OutputData outputData,
                                                GoalController goalController, StatisticsController statisticsController) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();

        outputData.output("Введите название цели:");
        String name = inputData.in().toString();

        if (goalController.getGoals(userId).stream().anyMatch(entity -> entity.getName().equals(name))) {
            outputData.output("У вас уже есть цель с таким названием. Желаете изменить её? Да/Нет");
            if (inputData.in().toString().equalsIgnoreCase("Да")){
                return handleUpdateGoal(inputData, outputData, goalController, statisticsController);
            }
            return UserLocation.GOALS_MENU_LOC;
        }

        outputData.output("Введите сумму, которую хотите накопить:");
        double targetAmount = Double.parseDouble(inputData.in().toString());

        outputData.output("Желаете сделать цель активной? (связать с транзакциями) Да/Нет");
        boolean goalActive = inputData.in().toString().equalsIgnoreCase("Да");

        double currentAmount = askGoalCurrentBalance(outputData, inputData, statisticsController, userId);

        Goal goal = goalController.createGoal(userId, goalActive, name, targetAmount, currentAmount);

        outputData.output("Цель успешно создана: " + formatGoal(goal, goalController));
        return UserLocation.GOALS_MENU_LOC;
    }

    /**
     * Handles retrieving a specific goal.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @param controller The goal controller.
     * @return The next user location after viewing the goal.
     */
    public static UserLocation handleViewGoal(Input inputData, OutputData outputData, GoalController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();

        outputData.output("Введите название цели:");
        String nameGoal = inputData.in().toString();

        Goal goal = controller.getGoal(nameGoal, userId);

        if (goal != null) {
            outputData.output("Детали вашей цели: " + formatGoal(goal, controller));
        } else {
            outputData.output("Цель с таким названием не найдена.");
        }

        return UserLocation.GOALS_MENU_LOC;
    }

    /**
     * Handles deleting a goal.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @param controller The goal controller.
     * @return The next user location after goal deletion.
     */
    public static UserLocation handleDeleteGoal(Input inputData, OutputData outputData, GoalController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();

        outputData.output("Введите название цели, которую хотите удалить:");
        String nameGoal = inputData.in().toString();

        controller.deleteGoal(nameGoal, userId);
        outputData.output("Цель успешно удалена.");

        return UserLocation.GOALS_MENU_LOC;
    }

    /**
     * Handles updating an existing goal.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @param controller The goal controller.
     * @return The next user location after updating the goal.
     */
    public static UserLocation handleUpdateGoal(Input inputData, OutputData outputData, GoalController controller,
                                                StatisticsController statisticsController) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();

        outputData.output("Введите id цели, которую хотите обновить:");
        Long id = Long.parseLong(inputData.in().toString());

        outputData.output("Введите название цели для обновления:");
        String nameGoal = inputData.in().toString();

        outputData.output("Введите новую сумму, которую хотите накопить:");
        double targetAmount = Double.parseDouble(inputData.in().toString());

        double currentAmount = askGoalCurrentBalance(outputData, inputData, statisticsController, userId);

        outputData.output("Актуальна ли цель? (true/false):");
        boolean goalActual = Boolean.parseBoolean(inputData.in().toString());

        controller.updateGoal(id, userId, nameGoal, targetAmount, currentAmount, goalActual);
        outputData.output("Цель успешно обновлена.");

        return UserLocation.GOALS_MENU_LOC;
    }

    /**
     * Handles displaying the user's active savings goal.
     *
     * @param outputData The output data provider.
     * @param controller The goal controller.
     * @return The next user location after viewing the active goal.
     */
    public static UserLocation handleViewActualGoal(OutputData outputData, GoalController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        Optional<Goal> goal = controller.getActualGoalByUserId(userId);

        if (goal.isPresent()) {
            outputData.output("Ваша актуальная цель: " + formatGoal(goal.get(), controller));
        } else {
            outputData.output("У вас нет активных целей.");
        }

        return UserLocation.GOALS_MENU_LOC;
    }

    public static UserLocation handleViewAllGoals(OutputData outputData, GoalController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        List<Goal> goals = controller.getGoals(userId);

        if (!goals.isEmpty()) {
            goals.forEach(goal -> outputData.output("Ваша актуальная цель: "
                    + formatGoal(goal, controller)));
        } else {
            outputData.output("У вас нет созданных целей.");
        }

        return UserLocation.GOALS_MENU_LOC;
    }

    public static UserLocation handleDeactivateGoal(OutputData outputData,
                                                    GoalController controller){
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        controller.deactivateGoalByUserId(userId);
        outputData.output("Изменения сохранены! У вас нет больше активных целей");
        return UserLocation.GOALS_MENU_LOC;
    }

    /**
     * Handles displaying the progress of a specific goal.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @param controller The goal controller.
     * @return The next user location after viewing progress.
     */
    public static UserLocation handleViewGoalProgress(Input inputData, OutputData outputData, GoalController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();

        outputData.output("Введите название цели для отслеживания прогресса:");
        String nameGoal = inputData.in().toString();

        Goal goal = controller.getGoal(nameGoal, userId);

        if (goal != null) {
            double progress = controller.getGoalProgress(goal);
            outputData.output("Прогресс цели '" + goal.getName() + "': " + progress + "%");
        } else {
            outputData.output("Цель не найдена.");
        }

        return UserLocation.GOALS_MENU_LOC;
    }

    private static double askGoalCurrentBalance(OutputData outputData, Input inputData, StatisticsController controller,
                                                Long userId) {
        double currentBalance = controller.calculateCurrentBalance(userId);
        outputData.output(String.format("Ваш баланс составляет сейчас %.2f. " +
                "Желаете связать свой баланс с актуальной целью? Да/Нет", currentBalance));

        double currentAmount;
        if (inputData.in().toString().equalsIgnoreCase("Да")){
            currentAmount = currentBalance;
        }else {
            outputData.output("Введите сумму, которая накоплена:");
            currentAmount = Double.parseDouble(inputData.in().toString());
        }
        return currentAmount;
    }

    /**
     * Formats a goal for display.
     *
     * @param goal The goal to format.
     * @return A formatted string representation of the goal.
     */
    private static String formatGoal(Goal goal, GoalController controller) {
        String strCurrentAmount = goal.isGoalActual() ? "Накоплено" : "Было накоплено";
        String strProgress = goal.isGoalActual() ? "Прогресс" : "Сохранённый прогресс";
        return String.format("ID: %d | Название: %s | Целевая сумма: %.2f | %s: %.2f | Актуальна: %b | %s: %.2f%%",
                goal.getId(), goal.getName(), goal.getTargetAmount(), strCurrentAmount, goal.getCurrentAmount(), goal.isGoalActual(),
                strProgress, controller.getGoalProgress(goal));
    }
}

