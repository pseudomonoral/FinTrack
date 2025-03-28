package com.dao;

import com.financeTracker.dao.GoalDao;
import com.financeTracker.dao.UserDao;
import com.financeTracker.dao.impl.GoalDaoImpl;
import com.financeTracker.dao.impl.UserDaoImpl;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.model.entity.User;
import com.financeTracker.util.DatabaseManager;
import com.testRepository.TestDatabaseContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование GoalDao")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GoalDaoTest {
    private GoalDao goalDao;
    private UserDao userDao;

    @BeforeAll
    void setup() {
        DatabaseManager databaseManager = TestDatabaseContainer.getDatabaseManager();
        goalDao = new GoalDaoImpl(databaseManager.getDataSource());
        userDao = new UserDaoImpl(databaseManager.getDataSource());
    }

    private User createTestUser() {
        return userDao.save(User.builder()
                .name("Test User")
                .login("test_user_" + UUID.randomUUID())
                .password("password")
                .build());
    }

    private Goal createGoal(Long userId, String name) {
        return Goal.builder()
                .userId(userId)
                .name(name)
                .targetAmount(5000.00)
                .currentAmount(0.0)
                .build();
    }

    @Test
    @DisplayName("Цель должна сохраняться только для существующего пользователя")
    void saveGoal_ShouldPersistGoalForExistingUser() {
        User user = createTestUser();
        Goal goal = createGoal(user.getId(), "New Car");
        Goal savedGoal = goalDao.save(goal);

        assertNotNull(savedGoal.getId(), "ID должен быть установлен после сохранения");
        assertEquals(user.getId(), savedGoal.getUserId(), "UserId должен совпадать");
    }

    @Test
    @DisplayName("Цель должна обновляться корректно")
    void updateGoal_ShouldUpdateGoalFields() {
        User user = createTestUser();
        Goal goal = goalDao.save(createGoal(user.getId(), "Vacation"));

        goal.setTargetAmount(7000.00);
        goal.setCurrentAmount(2000.00);
        goal.setGoalCompleted(true);
        goal.setGoalActual(true);

        goalDao.updateGoal(goal);
        Optional<Goal> updatedGoal = goalDao.findById(goal.getId());

        assertTrue(updatedGoal.isPresent(), "Обновленная цель должна существовать");
        assertEquals(7000.00, updatedGoal.get().getTargetAmount(), "Целевая сумма должна обновиться");
        assertEquals(2000.00, updatedGoal.get().getCurrentAmount(), "Текущая сумма должна обновиться");
        assertTrue(updatedGoal.get().isGoalCompleted(), "Цель должна быть отмечена как завершенная");
        assertTrue(updatedGoal.get().isGoalActual(), "Цель должна быть активной");
    }

    @Test
    @DisplayName("Поиск всех целей пользователя должен возвращать корректные данные")
    void findAllByUserId_ShouldReturnGoalsForUser() {
        User user = createTestUser();
        goalDao.save(createGoal(user.getId(), "House"));
        goalDao.save(createGoal(user.getId(), "Car"));

        List<Goal> goals = goalDao.findAllByUserId(user.getId());

        assertFalse(goals.isEmpty(), "Список целей не должен быть пустым");
        assertEquals(2, goals.size(), "Должно быть 2 сохраненные цели");
        assertTrue(goals.stream().anyMatch(g -> g.getName().equals("House")), "Цель 'House' должна существовать");
        assertTrue(goals.stream().anyMatch(g -> g.getName().equals("Car")), "Цель 'Car' должна существовать");
    }

    @Test
    @DisplayName("Поиск цели по имени и userId должен работать корректно")
    void findGoalByNameAndId_ShouldReturnGoalIfExists() {
        User user = createTestUser();
        Goal goal = goalDao.save(createGoal(user.getId(), "Education"));

        Optional<Goal> foundGoal = goalDao.findGoalByNameAndId("Education", user.getId());

        assertTrue(foundGoal.isPresent(), "Цель должна быть найдена по имени и userId");
        assertEquals(goal.getId(), foundGoal.get().getId(), "ID найденной цели должен совпадать");
    }

    @Test
    @DisplayName("Поиск цели по ID должен работать корректно")
    void findById_ShouldReturnGoalIfExists() {
        User user = createTestUser();
        Goal goal = goalDao.save(createGoal(user.getId(), "Health"));

        Optional<Goal> foundGoal = goalDao.findById(goal.getId());

        assertTrue(foundGoal.isPresent(), "Цель должна быть найдена по ID");
        assertEquals(goal.getId(), foundGoal.get().getId(), "ID найденной цели должен совпадать");
    }

    @Test
    @DisplayName("Метод findAll() должен возвращать список всех целей")
    void findAll_ShouldReturnListOfGoals() {
        User user = createTestUser();
        goalDao.save(createGoal(user.getId(), "Fitness"));
        goalDao.save(createGoal(user.getId(), "Travel"));

        List<Goal> goals = goalDao.findAll();

        assertFalse(goals.isEmpty(), "Список целей не должен быть пустым");
        assertTrue(goals.size() >= 2, "Список должен содержать как минимум 2 цели");
    }

    @Test
    @DisplayName("Удаление цели должно работать корректно")
    void deleteGoal_ShouldRemoveGoal() {
        User user = createTestUser();
        Goal goal = goalDao.save(createGoal(user.getId(), "Investment"));

        goalDao.delete(goal.getId());
        Optional<Goal> deletedGoal = goalDao.findById(goal.getId());

        assertFalse(deletedGoal.isPresent(), "Цель должна быть удалена");
    }
}

