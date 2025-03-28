package com.financeTracker.handler;

import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.TransactionController;
import com.financeTracker.controller.UserController;
import com.financeTracker.in.Input;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.UserLocation;
import com.financeTracker.out.OutputData;

import java.util.ArrayList;
import java.util.List;

import static com.financeTracker.model.type.UserLocation.ADMIN_MENU_LOC;
import static com.financeTracker.model.type.UserLocation.MAIN_MENU_LOC;
import static com.financeTracker.model.type.UserRole.ADMIN;


public class AdminHandler {

    /**
     * Handles checking admin access before executing an action.
     *
     * @param outputData  The output data provider.
     * @param controller  The user controller.
     * @return true if the user has admin access, false otherwise.
     */
    private static boolean checkAdminAccess(OutputData outputData, UserController controller) {
        if (!controller.checkAdminAccess(ApplicationContext.getAuthorizeUser().getId())) {
            outputData.output("Вы не имеете прав доступа!");
            return false;
        }
        return true;
    }

    /**
     * Prompts the admin to enter a user ID.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @return The user ID entered by the admin.
     */
    private static Long getUserId(Input inputData, OutputData outputData) {
        outputData.output("Введите id пользователя:");
        return Long.parseLong(inputData.in().toString());
    }

    /**
     * Handles the request to show the list of registered users.
     *
     * @param outputData The output data provider.
     * @param userController The main controller for handling business logic.
     */
    public static UserLocation handleShowListOfRegisteredUsers(OutputData outputData, UserController userController) {
        if (!checkAdminAccess(outputData, userController)) return MAIN_MENU_LOC;

        List<User> userList = userController.showAllUsers();
        List<User> adminList = new ArrayList<>();

        for (User user : userList) {
            if (user.getUserRole() == ADMIN) {
                adminList.add(user);
            } else {
                outputData.output(formatUser(user));
            }
        }
        adminList.forEach(admin -> outputData.output(formatUser(admin)));

        return ADMIN_MENU_LOC;
    }

    /**
     * Handles displaying all transactions by a specific user.
     *
     * @param inputData            The input data provider.
     * @param outputData           The output data provider.
     * @param userController       The user controller.
     * @param transactionController The transaction controller.
     */
    public static UserLocation handleShowAllTransactionsByUser(Input inputData, OutputData outputData,
                                                               UserController userController, TransactionController transactionController) {
        if (!checkAdminAccess(outputData, userController)) return MAIN_MENU_LOC;

        Long userId = getUserId(inputData, outputData);
        transactionController.getTransactions(userId)
                .forEach(outputData::output);

        return ADMIN_MENU_LOC;
    }

    /**
     * Handles blocking a user by ID.
     *
     * @param inputData    The input data provider.
     * @param outputData   The output data provider.
     * @param userController The user controller.
     */
    public static UserLocation handleBlockUser(Input inputData, OutputData outputData, UserController userController) {
        if (!checkAdminAccess(outputData, userController)) return MAIN_MENU_LOC;

        Long userId = getUserId(inputData, outputData);
        userController.blockedUserById(userId);

        return ADMIN_MENU_LOC;
    }

    /**
     * Handles unblocking a user by ID.
     *
     * @param inputData    The input data provider.
     * @param outputData   The output data provider.
     * @param userController The user controller.
     */
    public static UserLocation handleUnblockUser(Input inputData, OutputData outputData, UserController userController) {
        if (!checkAdminAccess(outputData, userController)) return MAIN_MENU_LOC;

        Long userId = getUserId(inputData, outputData);
        userController.unblockedUserById(userId);

        return ADMIN_MENU_LOC;
    }

    /**
     * Formats the user details into a readable string.
     *
     * @param user The user to format.
     * @return A formatted string representing the user.
     */
    private static String formatUser(User user) {
        return String.format("login - %s | id - %s | registration date - %s | %s",
                user.getLogin(), user.getId(), user.getCreatedAt(), user.getUserRole());
    }
}

