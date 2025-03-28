package com.financeTracker.dao;


import com.financeTracker.dao.impl.BudgetDaoImpl;
import com.financeTracker.dao.impl.UserDaoImpl;
import com.financeTracker.model.entity.Budget;
import com.financeTracker.model.entity.User;
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

    @Test
    @DisplayName("Бюджет должен обновляться корректно")
    void updateBudget_ShouldUpdateBudgetFields() {
        User user = createTestUser();
        Budget budget = budgetDao.save(createBudget(user.getId()));

        budget.setLimit(5000.00);
        budgetDao.updateBudget(budget);
        Optional<Budget> updatedBudget = budgetDao.findById(budget.getId());

        assertTrue(updatedBudget.isPresent(), "Обновленный бюджет должен существовать");
        assertEquals(5000.00, updatedBudget.get().getLimit(), "Лимит бюджета должен обновиться");
    }

    @Test
    @DisplayName("Поиск бюджета по userId должен работать корректно")
    void findByUserId_ShouldReturnBudgetIfExists() {
        User user = createTestUser();
        Budget budget = budgetDao.save(createBudget(user.getId()));

        Optional<Budget> foundBudget = budgetDao.findByUserId(user.getId());

        assertTrue(foundBudget.isPresent(), "Бюджет должен быть найден по userId");
        assertEquals(budget.getId(), foundBudget.get().getId(), "ID найденного бюджета должен совпадать");
    }

    @Test
    @DisplayName("Поиск бюджета по ID должен работать корректно")
    void findById_ShouldReturnBudgetIfExists() {
        User user = createTestUser();
        Budget budget = budgetDao.save(createBudget(user.getId()));

        Optional<Budget> foundBudget = budgetDao.findById(budget.getId());

        assertTrue(foundBudget.isPresent(), "Бюджет должен быть найден по ID");
        assertEquals(budget.getId(), foundBudget.get().getId(), "ID найденного бюджета должен совпадать");
    }

    @Test
    @DisplayName("Метод findAll() должен возвращать список всех бюджетов")
    void findAll_ShouldReturnListOfBudgets() {
        User user1 = createTestUser();
        User user2 = createTestUser();
        budgetDao.save(createBudget(user1.getId()));
        budgetDao.save(createBudget(user2.getId()));

        List<Budget> budgets = budgetDao.findAll();

        assertFalse(budgets.isEmpty(), "Список бюджетов не должен быть пустым");
        assertTrue(budgets.size() >= 2, "Список должен содержать как минимум 2 бюджета");
    }

    @Test
    @DisplayName("Удаление бюджета должно работать корректно")
    void deleteBudget_ShouldRemoveBudget() {
        User user = createTestUser();
        Budget budget = budgetDao.save(createBudget(user.getId()));

        budgetDao.delete(budget.getId());
        Optional<Budget> deletedBudget = budgetDao.findById(budget.getId());

        assertFalse(deletedBudget.isPresent(), "Бюджет должен быть удален");
    }
}

