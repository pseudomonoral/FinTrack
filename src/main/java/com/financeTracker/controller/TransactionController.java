package com.financeTracker.controller;

import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Creates a new transaction for the user.
     *
     * @param amount      The amount of the transaction.
     * @param category    The category of the transaction.
     * @param description The description of the transaction.
     * @param type        The type of the transaction.
     * @return The created transaction.
     */
    public Transaction createTransaction(double amount, String category, String description, TransactionType type) {
        return transactionService.createTransaction(amount, category, description, type);
    }

    /**
     * Deletes a transaction by its ID.
     *
     * @param transactionId The ID of the transaction.
     */
    public void deleteTransaction(Long transactionId) {
        transactionService.deleteTransaction(transactionId);
    }

    /**
     * Updates an existing transaction with new details.
     *
     * @param amount     The new amount of the transaction.
     * @param category   The new category of the transaction.
     * @param description A description of the transaction.
     * @param type       The type of the transaction (e.g., INCOME, EXPENSE).
     */
    public void updateTransaction(Long id, double amount, String category, String description, TransactionType type) {
        transactionService.updateTransaction(id, amount, category, description, type);
    }

    /**
     * Gets all transactions for the user.
     *
     * @param userId The user's ID.
     * @return A list of transactions for the user.
     */
    public List<Transaction> getTransactions(Long userId) {
        return transactionService.getTransactions(userId);
    }

    /**
     * Gets all transactions.
     *
     * @return A list of all transactions.
     */
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    /**
     * Gets the transactions for the user on a specific date.
     *
     * @param userId The user's ID.
     * @param date   The date to filter the transactions.
     * @return A list of transactions on the specified date.
     */
    public List<Transaction> getTransactionsByDate(Long userId, LocalDateTime date) {
        return transactionService.getTransactionsByDate(userId, date);
    }

    /**
     * Gets the transactions for the user by category.
     *
     * @param userId The user's ID.
     * @param category The category to filter the transactions.
     * @return A list of transactions in the specified category.
     */
    public List<Transaction> getTransactionsByCategory(Long userId, String category) {
        return transactionService.getTransactionsByCategory(userId, category);
    }

    /**
     * Gets the transactions for the user by type.
     *
     * @param userId The user's ID.
     * @param type   The type of the transaction.
     * @return A list of transactions of the specified type.
     */
    public List<Transaction> getTransactionsByType(Long userId, TransactionType type) {
        return transactionService.getTransactionsByType(userId, type);
    }
}

