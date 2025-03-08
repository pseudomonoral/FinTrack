package com.financeTracker.dao;

import com.financeTracker.model.entity.Transaction;

/**
 * - Редактирование транзакции: Возможность изменения суммы, категории и описания.
 * - Удаление транзакции: Удаление конкретной записи из списка.
 *   Общее:
 * - Просмотр транзакций: Возвращение списка всех транзакций пользователя с возможностью фильтрации по дате, категории или типу (доход/расход).
 * - Создание транзакции: Пользователь может добавить доход или расход, указав сумму, категорию, дату и описание.
 */
public interface TransactionDao extends MainDao<Long, Transaction>{

    Transaction updateTransaction(Transaction transaction);

    Transaction deleteTransaction(Transaction transaction);

}
