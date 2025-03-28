package com.financeTracker;

import com.financeTracker.controller.*;
import com.financeTracker.exceptions.authorization.AuthorizationException;
import com.financeTracker.exceptions.registration.RegistrationException;
import com.financeTracker.exceptions.validation.NotValidArgumentException;
import com.financeTracker.handler.*;
import com.financeTracker.in.Input;
import com.financeTracker.model.type.UserLocation;
import com.financeTracker.out.OutputData;

import static com.financeTracker.handler.SecurityHandler.handleAuthorization;
import static com.financeTracker.handler.SecurityHandler.handleRegistration;
import static com.financeTracker.model.type.UserLocation.*;
import static com.financeTracker.util.ConsoleConstants.*;
import static com.financeTracker.util.ConsoleConstants.ADMIN_MENU;
import static com.financeTracker.util.ConsoleConstants.MAIN_MENU;


/**
 * The ApplicationRunner class is responsible for running the main loop of the application.
 * It handles user input, manages different user locations, and delegates tasks to appropriate handlers.
 */
public class ApplicationRunner {

    private static AuthController authController;
    private static BudgetController budgetController;
    private static GoalController goalController;
    private static StatisticsController statisticsController;
    private static TransactionController transactionController;
    private static UserLocation userLocation;
    private static UserController userController;

    public static void run() {
        ApplicationContext.loadContext();
        Input inputData = (Input) ApplicationContext.getBean("input");
        OutputData outputData = (OutputData) ApplicationContext.getBean("output");
        authController = (AuthController) ApplicationContext.getBean("authController");
        budgetController = (BudgetController) ApplicationContext.getBean("budgetController");
        goalController = (GoalController) ApplicationContext.getBean("goalController");
        statisticsController = (StatisticsController) ApplicationContext.getBean("statisticsController");
        transactionController = (TransactionController) ApplicationContext.getBean("transactionController");
        userController = (UserController) ApplicationContext.getBean("userController");

        userLocation = SECURITY_LOC;

        boolean processIsRun = true;
        while (processIsRun) {
            try {
                switch (userLocation) {
                    case SECURITY_LOC -> handleSecurity(inputData, outputData);
                    case MAIN_MENU_LOC -> handleMainMenu(inputData, outputData);
                    case ADMIN_MENU_LOC -> handleAdminMenu(inputData, outputData);
                    case USER_SETTINGS_LOC -> handleUserSettings(inputData, outputData);
                    case BUDGET_MENU_LOC -> handleBudgetMenu(inputData, outputData);
                    case GOALS_MENU_LOC -> handleGoalMenu(inputData, outputData);
                    case TRANSACTION_MENU_LOC -> handleTransactionMenu(inputData, outputData);
                    case STATISTICS_MENU_LOC -> handleStatisticsMenu(inputData, outputData);
                    case EXIT -> {
                        exitProcess(outputData);
                        processIsRun = false;
                    }
                }
            } catch (AuthorizationException |
                     NotValidArgumentException |
                     RegistrationException e) {
                outputData.errOutput(e.getMessage());
            } catch (RuntimeException e) {
                outputData.errOutput("Unknown error. More details: " + e.getMessage());
                processIsRun = false;
            }
        }
        inputData.closeIn();
    }

    /**
     * Handles the main menu interactions, where the user can navigate to other menu sections.
     *
     * @param inputData The input interface to handle user input.
     * @param outputData The output interface to display information to the user.
     */
    private static void handleMainMenu(Input inputData, OutputData outputData) {
        while (true) {
            outputData.output(MAIN_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> {
                    userLocation = USER_SETTINGS_LOC;
                    return;
                }
                case "2" -> {
                    userLocation = BUDGET_MENU_LOC;
                    return;
                }
                case "3" -> {
                    userLocation = GOALS_MENU_LOC;
                    return;
                }
                case "4" -> {
                    userLocation = TRANSACTION_MENU_LOC;
                    return;
                }
                case "5" -> {
                    userLocation = STATISTICS_MENU_LOC;
                    return;
                }
                case "6" -> {
                    userLocation = ADMIN_MENU_LOC;
                    return;
                }
                case "0" -> {
                    userLocation = EXIT;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }


    /**
     * Handles security menu interactions, where users can choose to register, authorize, or exit.
     *
     * @param inputData The input interface to handle user input.
     * @param outputData The output interface to display information to the user.
     */
    public static void handleSecurity(Input inputData, OutputData outputData) {
        while (true) {
            outputData.output(SECURITY_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> {
                    userLocation = handleRegistration(inputData, outputData, authController);
                    return;
                }
                case "2" -> {
                    userLocation = handleAuthorization(inputData, outputData, authController);
                    return;
                }
                case "3" -> {
                    userLocation = EXIT;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }

    /**
     * Handles the user menu interactions, allowing the user to edit profile or delete it.
     *
     * @param inputData The input interface to handle user input.
     * @param outputData The output interface to display information to the user.
     */
    private static void handleUserSettings(Input inputData, OutputData outputData) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        while (true) {
            outputData.output(USER_SETTINGS_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> {
                    userLocation = UserSettingsHandler.editProfileName(inputData, outputData, userController, userId);
                    return;
                }
                case "2" -> {
                    userLocation = UserSettingsHandler.resetPasswordAndEmail(inputData, outputData, userController, userId);
                    return;
                }
                case "3" -> {
                    userLocation = UserSettingsHandler.deleteProfile(inputData, outputData, userController, userId);
                    return;
                }
                case "0" -> {
                    userLocation = MAIN_MENU_LOC;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }


    /**
     * Handles the admin menu interactions, allowing the admin to perform various operations such as viewing users,
     * managing transactions, and blocking/unblocking users.
     *
     * @param inputData The input interface to handle user input.
     * @param outputData The output interface to display information to the user.
     */
    private static void handleAdminMenu(Input inputData, OutputData outputData) {
        while (true) {
            outputData.output(ADMIN_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> {
                    userLocation = AdminHandler.handleShowListOfRegisteredUsers(outputData, userController);
                    return;
                }
                case "2" -> {
                    userLocation = AdminHandler.handleShowAllTransactionsByUser(inputData, outputData, userController, transactionController);
                    return;
                }
                case "3" -> {
                    userLocation = AdminHandler.handleBlockUser(inputData, outputData, userController);
                    return;
                }
                case "4" -> {
                    userLocation = AdminHandler.handleUnblockUser(inputData, outputData, userController);
                    return;
                }
                case "0" -> {
                    userLocation = MAIN_MENU_LOC;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }

    /**
     * Handles the budget menu interactions, allowing the user to create, view, update, delete, or check budget limits.
     *
     * @param inputData The input interface to handle user input.
     * @param outputData The output interface to display information to the user.
     */
    private static void handleBudgetMenu(Input inputData, OutputData outputData) {
        while (true) {
            outputData.output(BUDGET_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> {
                    userLocation = BudgetHandler.handleCreateBudget(inputData, outputData, budgetController);
                    return;
                }
                case "2" -> {
                    userLocation = BudgetHandler.handleViewBudget(outputData, budgetController);
                    return;
                }
                case "3" -> {
                    userLocation = BudgetHandler.handleUpdateBudget(inputData, outputData, budgetController);
                    return;
                }
                case "4" -> {
                    userLocation = BudgetHandler.handleDeleteBudget(outputData, budgetController);
                    return;
                }
                case "5" -> {
                    userLocation = BudgetHandler.handleCheckBudgetExceeded(outputData, budgetController, statisticsController);
                    return;
                }
                case "0" -> {
                    userLocation = MAIN_MENU_LOC;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }

    /**
     * Handles the goal menu interactions, allowing the user to create, view, update, delete, or check progress on goals.
     *
     * @param inputData The input interface to handle user input.
     * @param outputData The output interface to display information to the user.
     */
    private static void handleGoalMenu(Input inputData, OutputData outputData) {
        while (true) {
            outputData.output(GOAL_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> {
                    userLocation = GoalHandler.handleCreateGoal(inputData, outputData, goalController, statisticsController);
                    return;
                }
                case "2" -> {
                    userLocation = GoalHandler.handleViewActualGoal(outputData, goalController);
                    return;
                }
                case "3" -> {
                    userLocation = GoalHandler.handleUpdateGoal(inputData, outputData, goalController, statisticsController);
                    return;
                }
                case "4" -> {
                    userLocation = GoalHandler.handleDeleteGoal(inputData, outputData, goalController);
                    return;
                }
                case "5" -> {
                    userLocation = GoalHandler.handleDeactivateGoal(outputData, goalController);
                    return;
                }
                case "6" -> {
                    userLocation = GoalHandler.handleViewGoalProgress(inputData, outputData, goalController);
                    return;
                }
                case "7" -> {
                    userLocation = GoalHandler.handleViewGoal(inputData, outputData, goalController);
                    return;
                }
                case "8" -> {
                    userLocation = GoalHandler.handleViewAllGoals(outputData, goalController);
                    return;
                }
                case "0" -> {
                    userLocation = MAIN_MENU_LOC;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }

    /**
     * Handles the transaction menu interactions, allowing the user to create, view, update, delete, or filter transactions.
     *
     * @param inputData The input interface to handle user input.
     * @param outputData The output interface to display information to the user.
     */
    private static void handleTransactionMenu(Input inputData, OutputData outputData) {
        while (true) {
            outputData.output(TRANSACTION_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> {
                    userLocation = TransactionHandler.handleCreateTransaction(inputData, outputData, transactionController);
                    return;
                }
                case "2" -> {
                    userLocation = TransactionHandler.handleViewTransactions(outputData, transactionController);
                    return;
                }
                case "3" -> {
                    userLocation = TransactionHandler.handleUpdateTransaction(inputData, outputData, transactionController);
                    return;
                }
                case "4" -> {
                    userLocation = TransactionHandler.handleDeleteTransaction(inputData, outputData, transactionController);
                    return;
                }
                case "5" -> {
                    userLocation = TransactionHandler.handleFilterTransactionsByCategory(inputData, outputData, transactionController);
                    return;
                }
                case "0" -> {
                    userLocation = MAIN_MENU_LOC;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }

    /**
     * Handles the statistics menu interactions, allowing the user to view current balance, income, expenses, or expense analysis.
     *
     * @param inputData The input interface to handle user input.
     * @param outputData The output interface to display information to the user.
     */
    private static void handleStatisticsMenu(Input inputData, OutputData outputData) {
        while (true) {
            outputData.output(STATISTICS_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> {
                    userLocation = StatisticsHandler.handleCurrentBalance(inputData, outputData, statisticsController);
                    return;
                }
                case "2" -> {
                    userLocation = StatisticsHandler.handleIncomeForPeriod(inputData, outputData, statisticsController);
                    return;
                }
                case "3" -> {
                    userLocation = StatisticsHandler.handleExpenseForPeriod(inputData, outputData, statisticsController);
                    return;
                }
                case "4" -> {
                    userLocation = StatisticsHandler.handleExpenseAnalysis(inputData, outputData, statisticsController);
                    return;
                }
                case "0" -> {
                    userLocation = MAIN_MENU_LOC;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }

    private static void exitProcess(OutputData outputData) {
        outputData.output("До свидания!");
        ApplicationContext.cleanAuthorizedUser();
    }
}

