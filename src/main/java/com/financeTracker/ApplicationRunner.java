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
                    case USER_MENU_LOC -> handleUserMenu(inputData, outputData);
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

    private static void handleMainMenu(Input inputData, OutputData outputData) {
        while (true) {
            outputData.output(MAIN_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> userLocation = USER_MENU_LOC;
                case "2" -> userLocation = BUDGET_MENU_LOC;
                case "3" -> userLocation = GOALS_MENU_LOC;
                case "4" -> userLocation = TRANSACTION_MENU_LOC;
                case "5" -> userLocation = STATISTICS_MENU_LOC;
                case "0" -> {
                    userLocation = SECURITY_LOC;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }

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

    private static void handleUserMenu(Input inputData, OutputData outputData) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        while (true) {
            outputData.output(USER_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> userLocation = UserHandler.editProfileName(inputData, outputData, userController, userId);
                case "2" -> userLocation = UserHandler.resetPasswordAndEmail(inputData, outputData, userController, userId);
                case "3" -> userLocation = UserHandler.deleteProfile(inputData, outputData, userController, userId);
                case "0" -> {
                    userLocation = MAIN_MENU_LOC;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }

    private static void handleAdminMenu(Input inputData, OutputData outputData) {
        while (true) {
            outputData.output(ADMIN_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> AdminHandler.handleShowListOfRegisteredUsers(outputData, userController);
                case "2" -> AdminHandler.handleShowAllTransactionsByUser(inputData, outputData, transactionController);
                case "3" -> AdminHandler.handleBlockUser(inputData, outputData, userController);
                case "4" -> AdminHandler.handleUnblockUser(inputData, outputData, userController);
                case "0" -> {
                    userLocation = SECURITY_LOC;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }

    private static void handleBudgetMenu(Input inputData, OutputData outputData) {
        while (true) {
            outputData.output(BUDGET_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> userLocation = BudgetHandler.handleCreateBudget(inputData, outputData, budgetController);
                case "2" -> userLocation = BudgetHandler.handleViewBudget(outputData, budgetController);
                case "3" -> userLocation = BudgetHandler.handleUpdateBudget(inputData, outputData, budgetController);
                case "4" -> userLocation = BudgetHandler.handleDeleteBudget(outputData, budgetController);
                case "5" -> userLocation = BudgetHandler.handleCheckBudgetExceeded(outputData, budgetController,
                        statisticsController);
                case "0" -> {
                    userLocation = MAIN_MENU_LOC;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }

    private static void handleGoalMenu(Input inputData, OutputData outputData) {
        while (true) {
            outputData.output(GOAL_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> userLocation = GoalHandler.handleCreateGoal(inputData, outputData, goalController);
                case "2" -> userLocation = GoalHandler.handleViewGoal(inputData, outputData, goalController);
                case "3" -> userLocation = GoalHandler.handleUpdateGoal(inputData, outputData, goalController);
                case "4" -> userLocation = GoalHandler.handleDeleteGoal(inputData, outputData, goalController);
                case "5" -> userLocation = GoalHandler.handleViewGoalProgress(inputData, outputData, goalController);
                case "0" -> {
                    userLocation = MAIN_MENU_LOC;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }

    private static void handleTransactionMenu(Input inputData, OutputData outputData) {
        while (true) {
            outputData.output(TRANSACTION_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> userLocation = TransactionHandler.handleCreateTransaction(inputData, outputData, transactionController);
                case "2" -> userLocation = TransactionHandler.handleViewTransactions(outputData, transactionController);
                case "3" -> userLocation = TransactionHandler.handleUpdateTransaction(inputData, outputData, transactionController);
                case "4" -> userLocation = TransactionHandler.handleDeleteTransaction(inputData, outputData, transactionController);
                case "5" -> userLocation = TransactionHandler.handleFilterTransactionsByCategory(inputData, outputData, transactionController);
                case "0" -> {
                    userLocation = MAIN_MENU_LOC;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }

    private static void handleStatisticsMenu(Input inputData, OutputData outputData) {
        while (true) {
            outputData.output(STATISTICS_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> userLocation = StatisticsHandler.handleCurrentBalance(inputData, outputData, statisticsController);
                case "2" -> userLocation = StatisticsHandler.handleIncomeForPeriod(inputData, outputData, statisticsController);
                case "3" -> userLocation = StatisticsHandler.handleExpenseForPeriod(inputData, outputData, statisticsController);
                case "4" -> userLocation = StatisticsHandler.handleExpenseAnalysis(inputData, outputData, statisticsController);
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

