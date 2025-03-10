package com.financeTracker.handler;

import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.TransactionController;
import com.financeTracker.in.Input;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.model.type.UserLocation;
import com.financeTracker.out.OutputData;

import java.util.List;

import static com.financeTracker.model.type.TransactionType.*;

public class TransactionHandler {

    /**
     * Handles the creation of a new transaction.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @param controller The transaction controller.
     * @return The next user location after transaction creation.
     */
    public static UserLocation handleCreateTransaction(Input inputData, OutputData outputData, TransactionController controller) {
        outputData.output("Введите сумму транзакции:");
        double amount = Double.parseDouble(inputData.in().toString());

        outputData.output("Введите категорию транзакции:");
        String category = inputData.in().toString();

        outputData.output("Введите описание транзакции:");
        String description = inputData.in().toString();

        outputData.output("Выберите тип транзакции (INCOME/EXPENSE):");
        String typeInput = inputData.in().toString().toUpperCase();
        TransactionType type = valueOf(typeInput);

        controller.createTransaction(amount, category, description, type);

        outputData.output("Транзакция успешно создана.");
        return UserLocation.TRANSACTION_MENU;
    }

    /**
     * Handles the deletion of a transaction.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @param controller The transaction controller.
     * @return The next user location after transaction deletion.
     */
    public static UserLocation handleDeleteTransaction(Input inputData, OutputData outputData, TransactionController controller) {
        outputData.output("Введите ID транзакции для удаления:");
        Long transactionId = Long.parseLong(inputData.in().toString());

        controller.deleteTransaction(transactionId);

        outputData.output("Транзакция успешно удалена.");
        return UserLocation.TRANSACTION_MENU;
    }

    /**
     * Handles the update of a transaction.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @param controller The transaction controller.
     * @return The next user location after transaction update.
     */
    public static UserLocation handleUpdateTransaction(Input inputData, OutputData outputData, TransactionController controller) {
        outputData.output("Введите ID транзакции для обновления:");
        Long transactionId = Long.parseLong(inputData.in().toString());

        outputData.output("Введите новую сумму транзакции:");
        double amount = Double.parseDouble(inputData.in().toString());

        outputData.output("Введите новую категорию транзакции:");
        String category = inputData.in().toString();

        outputData.output("Введите новое описание транзакции:");
        String description = inputData.in().toString();

        Transaction transaction = new Transaction(transactionId, amount, category, description,
                amount > 0 ? INCOME : EXPENSE);
        controller.updateTransaction(transaction);

        outputData.output("Транзакция успешно обновлена.");
        return UserLocation.TRANSACTION_MENU;
    }

    /**
     * Handles retrieving and displaying user transactions.
     *
     * @param outputData The output data provider.
     * @param controller The transaction controller.
     * @return The next user location after viewing transactions.
     */
    public static UserLocation handleViewTransactions(OutputData outputData, TransactionController controller) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        List<Transaction> transactions = controller.getTransactions(userId);

        if (transactions.isEmpty()) {
            outputData.output("У вас нет транзакций.");
        } else {
            for (Transaction transaction : transactions) {
                outputData.output(formatTransaction(transaction));
            }
        }

        return UserLocation.TRANSACTION_MENU;
    }

    /**
     * Handles filtering transactions by category.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @param controller The transaction controller.
     * @return The next user location after filtering transactions.
     */
    public static UserLocation handleFilterTransactionsByCategory(Input inputData, OutputData outputData, TransactionController controller) {
        outputData.output("Введите категорию для фильтрации транзакций:");
        String category = inputData.in().toString();

        Long userId = ApplicationContext.getAuthorizeUser().getId();
        List<Transaction> transactions = controller.getTransactionsByCategory(userId, category);

        if (transactions.isEmpty()) {
            outputData.output("Нет транзакций в данной категории.");
        } else {
            for (Transaction transaction : transactions) {
                outputData.output(formatTransaction(transaction));
            }
        }

        return UserLocation.TRANSACTION_MENU;
    }

    /**
     * Formats a transaction for display.
     *
     * @param transaction The transaction to format.
     * @return A formatted string representation of the transaction.
     */
    private static String formatTransaction(Transaction transaction) {
        return String.format("ID: %d | Сумма: %.2f | Категория: %s | Описание: %s | Дата: %s",
                transaction.getId(), transaction.getAmount(), transaction.getCategory(),
                transaction.getDescription(), transaction.getCreatedAt().toString());
    }
}
