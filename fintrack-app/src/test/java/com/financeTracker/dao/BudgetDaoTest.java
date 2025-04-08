package com.financeTracker.dao;

import com.financeTracker.dao.impl.BudgetDaoImpl;
import com.financeTracker.model.entity.Budget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.Assert.*;

class BudgetDaoTest extends AbstractDaoTest {

    private BudgetDaoImpl budgetDao;

    @BeforeEach
    void setUpDao() {
        budgetDao = new BudgetDaoImpl(jdbcTemplate);
    }

    @Override
    protected void setupSchema() {
        jdbcTemplate.execute("""
            CREATE SCHEMA IF NOT EXISTS fintrack;
            CREATE TABLE IF NOT EXISTS fintrack.budgets (
                id SERIAL PRIMARY KEY,
                user_id BIGINT NOT NULL,
                "limit" NUMERIC NOT NULL,
                created_at TIMESTAMP NOT NULL,
                updated_at TIMESTAMP NOT NULL
            );
        """);
    }

    @Test
    void save_shouldInsertAndReturnEntityWithId() {
        Budget budget = Budget.builder()
                .userId(1L)
                .limit(500.0)
                .build();

        Budget saved = budgetDao.save(budget);

        assertNotNull(saved.getId());

        assertEquals(Long.valueOf(1L), saved.getUserId());
        assertEquals(500.0, saved.getLimit(), 0.0001);
    }

    @Test
    void findById_shouldReturnSavedBudget() {
        Budget budget = Budget.builder()
                .userId(2L)
                .limit(300.0)
                .build();

        Budget saved = budgetDao.save(budget);

        Optional<Budget> found = budgetDao.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }

    @Test
    void findByUserId_shouldReturnCorrectBudget() {
        Budget budget = Budget.builder()
                .userId(999L)
                .limit(700.0)
                .build();

        budgetDao.save(budget);

        Optional<Budget> found = budgetDao.findByUserId(999L);

        assertTrue(found.isPresent());
        assertEquals(700.0, found.get().getLimit(), 0.0001);
    }

    @Test
    void delete_shouldRemoveBudget() {
        Budget budget = Budget.builder()
                .userId(123L)
                .limit(100.0)
                .build();

        Budget saved = budgetDao.save(budget);
        budgetDao.delete(saved.getId());

        Optional<Budget> deleted = budgetDao.findById(saved.getId());
        assertTrue(deleted.isEmpty());
    }

    @Test
    void updateBudget_shouldChangeLimit() {
        Budget budget = Budget.builder()
                .userId(321L)
                .limit(200.0)
                .build();

        Budget saved = budgetDao.save(budget);

        saved.setLimit(900.0);
        budgetDao.updateBudget(saved);

        Optional<Budget> updated = budgetDao.findById(saved.getId());

        assertTrue(updated.isPresent());
        assertEquals(900.0, updated.get().getLimit(), 0.0001);
    }
}

