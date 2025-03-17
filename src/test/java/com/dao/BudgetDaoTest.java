package com.dao;

import com.financeTracker.dao.BudgetDao;
import com.financeTracker.dao.UserDao;
import com.financeTracker.dao.impl.BudgetDaoImpl;
import com.financeTracker.dao.impl.UserDaoImpl;
import com.financeTracker.model.entity.Budget;
import com.financeTracker.model.entity.User;
import com.financeTracker.util.DatabaseManager;
import com.testRepository.TestDatabaseContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Тестирование BudgetDao")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BudgetDaoTest {
    private BudgetDao budgetDao;
    private UserDao userDao;

    @BeforeAll
    void setup() {
        DatabaseManager databaseManager = TestDatabaseContainer.getDatabaseManager();
        budgetDao = new BudgetDaoImpl(databaseManager.getDataSource());
        userDao = new UserDaoImpl(databaseManager.getDataSource());
    }

    private User createTestUser() {
        return userDao.save(User.builder()
                .name("Test User")
                .login("test_user_" + UUID.randomUUID())
                .password("password")
                .build());
    }

    private Budget createBudget(Long userId) {
        return Budget.builder()
                .userId(userId)
                .limit(3000.00)
                .build();
    }

    @Test
    @DisplayName("Бюджет должен сохраняться только для существующего пользователя")
    void saveBudget_ShouldPersistBudgetForExistingUser() {
        User user = createTestUser();
        Budget budget = createBudget(user.getId());
        Budget savedBudget = budgetDao.save(budget);

        assertNotNull(savedBudget.getId(), "ID должен быть установлен после сохранения");
        assertEquals(user.getId(), savedBudget.getUserId(), "UserId должен совпадать");
    }

}
