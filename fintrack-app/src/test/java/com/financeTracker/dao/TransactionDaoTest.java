package com.financeTracker.dao;

import com.financeTracker.dao.impl.TransactionDaoImpl;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.type.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionDaoTest extends AbstractDaoTest {

    private TransactionDaoImpl transactionDao;

    @BeforeEach
    void setUpDao() {
        transactionDao = new TransactionDaoImpl(jdbcTemplate);
    }

    @Override
    protected void setupSchema() {
        jdbcTemplate.execute("""
            CREATE SCHEMA IF NOT EXISTS fintrack;

            CREATE TABLE IF NOT EXISTS fintrack.transactions (
                id SERIAL PRIMARY KEY,
                user_id BIGINT NOT NULL,
                amount NUMERIC NOT NULL,
                category TEXT NOT NULL,
                type TEXT NOT NULL,
                description TEXT,
                created_at TIMESTAMP NOT NULL DEFAULT now(),
                updated_at TIMESTAMP NOT NULL DEFAULT now()
            );
        """);
    }

    @Test
    void saveAndFindById_shouldWorkCorrectly() {
        Transaction tx = Transaction.builder()
                .userId(1L)
                .amount(150.75)
                .category("Groceries")
                .type(TransactionType.EXPENSE)
                .description("Bought vegetables")
                .build();

        Transaction saved = transactionDao.save(tx);

        Optional<Transaction> fromDb = transactionDao.findById(saved.getId());
        assertTrue(fromDb.isPresent());

        Transaction actual = fromDb.get();
        assertEquals("Groceries", actual.getCategory());
        assertEquals(150.75, actual.getAmount(), 0.0001);
        assertEquals(TransactionType.EXPENSE, actual.getType());
        assertEquals("Bought vegetables", actual.getDescription());
    }

    @Test
    void findAllByUserId_shouldReturnCorrectList() {
        Transaction t1 = Transaction.builder().userId(2L).amount(20.0).category("Food").type(TransactionType.EXPENSE).description("Snack").build();
        Transaction t2 = Transaction.builder().userId(2L).amount(100.0).category("Utilities").type(TransactionType.EXPENSE).description("Electricity").build();

        transactionDao.save(t1);
        transactionDao.save(t2);

        List<Transaction> list = transactionDao.findAllByUserId(2L);
        assertEquals(2, list.size());
    }

    @Test
    void findByUserId_shouldReturnFirstMatch() {
        Transaction tx = Transaction.builder()
                .userId(3L)
                .amount(300.0)
                .category("Travel")
                .type(TransactionType.EXPENSE)
                .description("Bus ticket")
                .build();

        transactionDao.save(tx);

        Optional<Transaction> result = transactionDao.findByUserId(3L);
        assertTrue(result.isPresent());
        assertEquals("Travel", result.get().getCategory());
    }

    @Test
    void updateTransaction_shouldUpdateValues() {
        Transaction tx = Transaction.builder()
                .userId(4L)
                .amount(50.0)
                .category("Books")
                .type(TransactionType.EXPENSE)
                .description("Initial")
                .build();

        Transaction saved = transactionDao.save(tx);

        saved.setAmount(60.0);
        saved.setCategory("UpdatedBooks");
        saved.setType(TransactionType.INCOME);
        saved.setDescription("Updated");

        transactionDao.updateTransaction(saved);

        Optional<Transaction> updated = transactionDao.findById(saved.getId());
        assertTrue(updated.isPresent());
        assertEquals(60.0, updated.get().getAmount(), 0.0001);
        assertEquals("UpdatedBooks", updated.get().getCategory());
        assertEquals(TransactionType.INCOME, updated.get().getType());
        assertEquals("Updated", updated.get().getDescription());
    }

    @Test
    void delete_shouldRemoveTransaction() {
        Transaction tx = Transaction.builder()
                .userId(5L)
                .amount(80.0)
                .category("ToDelete")
                .type(TransactionType.EXPENSE)
                .description("Temp")
                .build();

        Transaction saved = transactionDao.save(tx);
        transactionDao.delete(saved.getId());

        Optional<Transaction> result = transactionDao.findById(saved.getId());
        assertTrue(result.isEmpty());
    }
}