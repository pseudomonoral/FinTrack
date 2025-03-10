package com.financeTracker.handler;

import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.GoalController;
import com.financeTracker.in.Input;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.model.type.UserLocation;
import com.financeTracker.out.OutputData;

import java.util.Optional;

public class GoalHandler {

    /**
     * Handles the creation of a savings goal.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @param controller The goal controller.
     * @return The next user location after goal creation.
     */
    public static UserLocation handleCreateGoal(Input inputData, OutputData outputData, GoalController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();

        outputData.output("Введите название цели:");
        String name = inputData.in().toString();

        outputData.output("Введите сумму накопления:");
        double targetAmount = Double.parseDouble(inputData.in().toString());

        Goal goal = controller.createGoal(userId, false, name, targetAmount);

        outputData.output("Цель успешно создана: " + formatGoal(goal, controller));
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
    public static UserLocation handleUpdateGoal(Input inputData, OutputData outputData, GoalController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();

        outputData.output("Введите название цели для обновления:");
        String nameGoal = inputData.in().toString();

        outputData.output("Введите новую сумму накопления:");
        double targetAmount = Double.parseDouble(inputData.in().toString());

        outputData.output("Актуальна ли цель? (true/false):");
        boolean goalActual = Boolean.parseBoolean(inputData.in().toString());

        controller.updateGoal(userId, nameGoal, targetAmount, goalActual);
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

    /**
     * Formats a goal for display.
     *
     * @param goal The goal to format.
     * @return A formatted string representation of the goal.
     */
    private static String formatGoal(Goal goal, GoalController controller) {

        return String.format("Название: %s | Сумма: %.2f | Актуальна: %b | Прогресс: %.2f%%",
                goal.getName(), goal.getTargetAmount(), goal.isGoalCompleted(), controller.getGoalProgress(goal));
    }
}

