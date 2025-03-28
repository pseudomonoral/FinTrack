package com.financeTracker.dao;

import com.financeTracker.dao.impl.TransactionDaoImpl;
import com.financeTracker.dao.impl.UserDaoImpl;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.testRepository.TestDatabaseContainer;
import com.financeTracker.util.DatabaseManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование TransactionDao")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionDaoTest {
    private TransactionDao transactionDao;
    private UserDao userDao;

    @BeforeAll
    void setup() {
        DatabaseManager databaseManager = TestDatabaseContainer.getDatabaseManager();
        transactionDao = new TransactionDaoImpl(databaseManager.getDataSource());
        userDao = new UserDaoImpl(databaseManager.getDataSource());
    }

    private User createTestUser() {
        return userDao.save(User.builder()
                .name("Test User")
                .login("test_user_" + UUID.randomUUID())
                .password("password")
                .build());
    }

    private Transaction createTransaction(Long userId) {
        return Transaction.builder()
                .userId(userId)
                .amount(150.75)
                .category("Food")
                .type(TransactionType.EXPENSE)
                .description("Lunch at restaurant")
                .build();
    }

    @Test
    @DisplayName("Транзакция должна сохраняться для существующего пользователя")
    void saveTransaction_ShouldPersistTransactionForExistingUser() {
        User user = createTestUser();
        Transaction transaction = createTransaction(user.getId());
        Transaction savedTransaction = transactionDao.save(transaction);

        assertNotNull(savedTransaction.getId(), "ID должен быть установлен после сохранения");
        assertEquals(user.getId(), savedTransaction.getUserId(), "UserId должен совпадать");
    }

    @Test
    @DisplayName("Транзакция должна обновляться корректно")
    void updateTransaction_ShouldUpdateTransactionFields() {
        User user = createTestUser();
        Transaction transaction = transactionDao.save(createTransaction(user.getId()));

        transaction.setAmount(200.50);
        transaction.setCategory("Shopping");
        transaction.setDescription("Bought clothes");
        transaction.setType(TransactionType.INCOME);

        transactionDao.updateTransaction(transaction);
        Optional<Transaction> updatedTransaction = transactionDao.findById(transaction.getId());

        assertTrue(updatedTransaction.isPresent(), "Обновленная транзакция должна существовать");
        assertEquals(200.50, updatedTransaction.get().getAmount(), "Сумма должна обновиться");
        assertEquals("Shopping", updatedTransaction.get().getCategory(), "Категория должна обновиться");
        assertEquals("Bought clothes", updatedTransaction.get().getDescription(), "Описание должно обновиться");
        assertEquals(TransactionType.INCOME, updatedTransaction.get().getType(), "Тип должен обновиться");
    }

    @Test
    @DisplayName("Поиск транзакций по userId должен возвращать корректные данные")
    void findByUserId_ShouldReturnTransactionsForUser() {
        User user = createTestUser();
        Transaction transaction1 = transactionDao.save(createTransaction(user.getId()));
        Transaction transaction2 = transactionDao.save(createTransaction(user.getId()));

        Optional<Transaction> transactions = transactionDao.findByUserId(user.getId());

        assertTrue(transactions.isPresent(), "Транзакции должны быть найдены");
        assertEquals(user.getId(), transactions.get().getUserId(), "UserId транзакции должен совпадать");
    }

    @Test
    @DisplayName("Поиск транзакции по ID должен работать корректно")
    void findById_ShouldReturnTransactionIfExists() {
        User user = createTestUser();
        Transaction transaction = transactionDao.save(createTransaction(user.getId()));

        Optional<Transaction> foundTransaction = transactionDao.findById(transaction.getId());

        assertTrue(foundTransaction.isPresent(), "Транзакция должна быть найдена по ID");
        assertEquals(transaction.getId(), foundTransaction.get().getId(), "ID найденной транзакции должен совпадать");
    }

    @Test
    @DisplayName("Метод findAll() должен возвращать список всех транзакций")
    void findAll_ShouldReturnListOfTransactions() {
        User user = createTestUser();
        transactionDao.save(createTransaction(user.getId()));
        transactionDao.save(createTransaction(user.getId()));

        List<Transaction> transactions = transactionDao.findAll();

        assertFalse(transactions.isEmpty(), "Список транзакций не должен быть пустым");
        assertTrue(transactions.size() >= 2, "Список должен содержать как минимум 2 транзакции");
    }

    @Test
    @DisplayName("Удаление транзакции должно работать корректно")
    void deleteTransaction_ShouldRemoveTransaction() {
        User user = createTestUser();
        Transaction transaction = transactionDao.save(createTransaction(user.getId()));

        transactionDao.delete(transaction.getId());
        Optional<Transaction> deletedTransaction = transactionDao.findById(transaction.getId());

        assertFalse(deletedTransaction.isPresent(), "Транзакция должна быть удалена");
    }
}
