package com.financeTracker.service;


import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.type.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The TransactionService interface provides methods for managing financial transactions.
 * It includes operations for creating, deleting, updating, and retrieving transactions.
 */
public interface TransactionService {

    /**
     * Creates a new transaction for a user.
     *
     * @param amount The amount of the transaction.
     * @param category The category of the transaction (e.g., food, rent, salary).
     * @param description A brief description of the transaction.
     * @param type The type of the transaction (income or expense).
     * @return Transaction The created transaction entity.
     */
    Transaction createTransaction(double amount, String category, String description, TransactionType type);

    /**
     * Deletes a specific transaction by its ID.
     *
     * @param transactionId The ID of the transaction to be deleted.
     */
    void deleteTransaction(Long transactionId);

    /**
     * Updates an existing transaction.
     *
     * @param transaction The transaction entity to be updated.
     */
    void updateTransaction(Transaction transaction);

    /**
     * Retrieves all transactions for a specific user.
     *
     * @param userId The ID of the user whose transactions are to be retrieved.
     * @return List<Transaction> A list of transactions associated with the given user ID.
     */
    List<Transaction> getTransactions(Long userId);

    /**
     * Retrieves all transactions in the system.
     *
     * @return List<Transaction> A list of all transactions in the system.
     */
    List<Transaction> getAllTransactions();

    /**
     * Retrieves all transactions for a specific user on a given date.
     *
     * @param userId The ID of the user whose transactions are to be retrieved.
     * @param date The date on which the transactions occurred.
     * @return List<Transaction> A list of transactions on the specified date.
     */
    List<Transaction> getTransactionsByDate(Long userId, LocalDateTime date);

    /**
     * Retrieves all transactions for a specific user in a given category.
     *
     * @param userId The ID of the user whose transactions are to be retrieved.
     * @param category The category of the transactions (e.g., food, entertainment).
     * @return List<Transaction> A list of transactions in the specified category.
     */
    List<Transaction> getTransactionsByCategory(Long userId, String category);

    /**
     * Retrieves all transactions for a specific user of a given type (income or expense).
     *
     * @param userId The ID of the user whose transactions are to be retrieved.
     * @param type The type of the transaction (income or expense).
     * @return List<Transaction> A list of transactions of the specified type.
     */
    List<Transaction> getTransactionsByType(Long userId, TransactionType type);
}

