package com.dao;

import com.financeTracker.dao.TransactionDao;
import com.financeTracker.dao.UserDao;
import com.financeTracker.dao.impl.TransactionDaoImpl;
import com.financeTracker.dao.impl.UserDaoImpl;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.util.DatabaseManager;
import com.testRepository.TestDatabaseContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}

