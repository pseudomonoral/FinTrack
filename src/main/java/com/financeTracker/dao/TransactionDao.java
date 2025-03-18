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

    void updateTransaction(Transaction transaction);

    Optional<Transaction> findByUserId(Long userId);

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id The identifier of the entity.
     * @return Optional<T> An Optional containing the entity if found, or an empty Optional if not found.
     */
    Optional<Transaction> findById(Long id);


    /**
     * Retrieves a list of all entities.
     *
     * @return List<T> A list of all entities of type T.
     */
    List<Transaction> findAll();

    /**
     * Saves or updates an entity in the database.
     *
     * @param entity The entity to be saved or updated.
     * @return T The saved entity with its updated state (e.g., with an assigned ID).
     */
    Transaction save(Transaction entity);

    /**
     * Deletes an entity from the database.
     *
     */
    void delete(Long transactionId);
}
