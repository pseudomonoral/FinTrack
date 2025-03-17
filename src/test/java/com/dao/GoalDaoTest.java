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
}
