package com.financeTracker.service;


import com.financeTracker.dto.transaction.TransactionRequestDto;
import com.financeTracker.dto.transaction.TransactionResponseDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for managing user financial transactions.
 * Provides operations for creating, updating, deleting, and retrieving transactions,
 * as well as applying business logic to budgets and goals based on transactions.
 */
public interface TransactionService {

    /**
     * Creates a new transaction for the specified user.
     * Automatically updates the user's budget and goal progress if applicable.
     *
     * @param userId                the ID of the user making the transaction
     * @param transactionRequestDto the DTO containing transaction data
     * @return the created transaction as a response DTO
     */
    TransactionResponseDto createTransaction(Long userId, TransactionRequestDto transactionRequestDto);

    /**
     * Deletes a transaction by its ID.
     * Reverts its effects on the user's budget and goal progress.
     *
     * @param transactionId the ID of the transaction to be deleted
     */
    void deleteTransaction(Long transactionId);

    /**
     * Updates an existing transaction for the given user.
     * If the transaction type or amount changes, its effects on budget and goal will be adjusted accordingly.
     *
     * @param userId                the ID of the user who owns the transaction
     * @param transactionId         the ID of the transaction to update
     * @param transactionRequestDto the DTO containing updated transaction data
     */
    void updateTransaction(Long userId, Long transactionId, TransactionRequestDto transactionRequestDto);

    /**
     * Retrieves all transactions made by a specific user.
     *
     * @param userId the ID of the user
     * @return a list of transaction response DTOs
     */
    List<TransactionResponseDto> getTransactionsByUserId(Long userId);

    /**
     * Retrieves all transactions in the system.
     *
     * @return a list of all transaction response DTOs
     */
    List<TransactionResponseDto> getAllTransactions();

    /**
     * Retrieves all transactions made by a user from a specific date onward.
     *
     * @param userId the ID of the user
     * @param date   the starting date (inclusive)
     * @return a list of transaction response DTOs
     */
    List<TransactionResponseDto> getTransactionsByDate(Long userId, LocalDateTime date);

    /**
     * Retrieves all transactions by a specific user that match a given category.
     *
     * @param userId   the ID of the user
     * @param category the transaction category to filter by
     * @return a list of transaction response DTOs
     */
    List<TransactionResponseDto> getTransactionsByCategory(Long userId, String category);

    /**
     * Retrieves all transactions by a specific user that match a given type (INCOME or EXPENSE).
     *
     * @param userId the ID of the user
     * @param type   the transaction type to filter by
     * @return a list of transaction response DTOs
     */
    List<TransactionResponseDto> getTransactionsByType(Long userId, String type);
}