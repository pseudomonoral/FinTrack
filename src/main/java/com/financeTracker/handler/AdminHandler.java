package com.financeTracker.handler;

import com.financeTracker.controller.TransactionController;
import com.financeTracker.controller.UserController;
import com.financeTracker.in.Input;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.entity.User;
import com.financeTracker.out.OutputData;

import java.util.ArrayList;
import java.util.List;

import static com.financeTracker.handler.SecurityHandler.isAdmin;


public class AdminHandler {

    /**
     * Handles the request to show the list of registered users.
     *
     * @param outputData The output data provider.
     * @param controller The main controller for handling business logic.
     */
    public static void handleShowListOfRegisteredUsers(OutputData outputData, UserController controller) {
        List<User> userList = controller.showAllUsers();
        List<User> adminList = new ArrayList<>();

        for (User user : userList) {
            if (isAdmin(user)) adminList.add(user);
            else {
                outputData.output(formatUser(user));
            }
        }
        for (User admin : adminList) {
            outputData.output(formatUser(admin));
        }
    }

    public static void handleShowAllTransactionsByUser(Input inputData, OutputData outputData, TransactionController transactionController) {
        final String idUserMessage = "Введите id пользователя:";
        outputData.output(idUserMessage);
        Long userId = Long.parseLong(inputData.in().toString());

        List<Transaction> transactions = transactionController.getTransactions(userId);
        for (Transaction transaction : transactions) {
            outputData.output(transaction);
        }
    }

    public static void handleBlockUser(Input inputData, OutputData outputData, UserController userController){
        final String idUserMessage = "Введите id пользователя:";
        outputData.output(idUserMessage);
        Long userId = Long.parseLong(inputData.in().toString());

        userController.blockedUserById(userId);
    }

    public static void handleUnblockUser(Input inputData, OutputData outputData, UserController userController){
        final String idUserMessage = "Введите id пользователя:";
        outputData.output(idUserMessage);
        Long userId = Long.parseLong(inputData.in().toString());

        userController.unblockedUserById(userId);
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
