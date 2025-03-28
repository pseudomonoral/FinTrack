package com.financeTracker.dao;

import com.financeTracker.model.entity.Transaction;

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
     * Finds all transactions associated with a specific user ID.
     *
     * @param userId The ID of the user whose transactions are to be retrieved.
     * @return Optional<Transaction> An Optional containing the transaction if found, or an empty Optional if not found.
     * @throws IllegalArgumentException if the provided userId is null.
     */
    Optional<Transaction> findByUserId(Long userId);

    /**
     * Finds a transaction by its unique identifier.
     *
     * @param id The unique identifier of the transaction.
     * @return Optional<Transaction> An Optional containing the transaction if found, or an empty Optional if not found.
     * @throws IllegalArgumentException if the provided ID is null.
     */
    Optional<Transaction> findById(Long id);

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
     * Deletes a transaction from the database by its ID.
     *
     * @param transactionId The unique identifier of the transaction to be deleted.
     * @throws IllegalArgumentException if the provided transactionId is null.
     */
    void delete(Long transactionId);
}

