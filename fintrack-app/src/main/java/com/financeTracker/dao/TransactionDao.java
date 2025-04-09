package com.financeTracker.dao;

import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.type.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * - Редактирование транзакции: Возможность изменения суммы, категории и описания.
 * - Удаление транзакции: Удаление конкретной записи из списка.
 * Общее:
 * - Просмотр транзакций: Возвращение списка всех транзакций пользователя с возможностью фильтрации по дате, категории или типу (доход/расход).
 * - Создание транзакции: Пользователь может добавить доход или расход, указав сумму, категорию, дату и описание.
 */
public interface TransactionDao {

    /**
     * Updates an existing transaction in the database.
     *
     * @param transaction The transaction entity containing updated data.
     * @throws IllegalArgumentException if the transaction does not exist or contains invalid data.
     */
    void updateTransaction(Transaction transaction);

    /**
     * Finds a transaction by its unique identifier.
     *
     * @param id The unique identifier of the transaction.
     * @return Optional<Transaction> An Optional containing the transaction if found, or an empty Optional if not found.
     * @throws IllegalArgumentException if the provided ID is null.
     */
    Optional<Transaction> findById(Long id);

    /**
     * Finds all transactions associated with a specific user ID.
     *
     * @param userId The ID of the user whose transactions are to be retrieved.
     * @return List<Transaction> A list of transactions for the specified user. If none exist, returns an empty list.
     * @throws IllegalArgumentException if the provided userId is null.
     */
    List<Transaction> findAllByUserId(Long userId);

    /**
     * Retrieves a list of all transactions in the database.
     *
     * @return List<Transaction> A list of all transactions. If no transactions exist, returns an empty list.
     */
    List<Transaction> findAll();

    /**
     * Saves a new transaction or updates an existing one in the database.
     *
     * @param transaction The transaction entity to be saved or updated.
     * @return Transaction The saved transaction entity with its assigned ID.
     * @throws IllegalArgumentException if the transaction or its required fields are null.
     */
    Transaction save(Transaction transaction);

    /**
     * Finds all transactions for a specific user that were created after or at a given date and time.
     *
     * @param userId The ID of the user.
     * @param date The minimum creation date and time for the transactions.
     * @return List<Transaction> A list of transactions matching the criteria. If none are found, returns an empty list.
     * @throws IllegalArgumentException if userId or date is null.
     */
    List<Transaction> findAllByUserIdAndDate(Long userId, LocalDateTime date);

    /**
     * Finds all transactions for a specific user and category.
     *
     * @param userId The ID of the user.
     * @param category The category to filter by.
     * @return List<Transaction> A list of matching transactions. If none are found, returns an empty list.
     * @throws IllegalArgumentException if userId or category is null.
     */
    List<Transaction> findAllByUserIdAndCategory(Long userId, String category);

    /**
     * Finds all transactions for a specific user and transaction type (INCOME or EXPENSE).
     *
     * @param userId The ID of the user.
     * @param type The type of transaction to filter by.
     * @return List<Transaction> A list of matching transactions. If none are found, returns an empty list.
     * @throws IllegalArgumentException if userId or type is null.
     */
    List<Transaction> findAllByUserIdAndType(Long userId, TransactionType type);

    /**
     * Calculates the total amount of transactions for a specific user and transaction type within a given time period.
     *
     * @param userId The ID of the user.
     * @param type The type of transaction (INCOME or EXPENSE).
     * @param from The start date and time of the period.
     * @param to The end date and time of the period.
     * @return double The total amount for the specified criteria. Returns 0.0 if no matching transactions are found.
     * @throws IllegalArgumentException if any of the parameters are null.
     */
    double calculateTotalForPeriod(Long userId, TransactionType type, LocalDateTime from, LocalDateTime to);

    /**
     * Deletes a transaction from the database by its ID.
     *
     * @param transactionId The unique identifier of the transaction to be deleted.
     * @throws IllegalArgumentException if the provided transactionId is null.
     */
    void delete(Long transactionId);
}
