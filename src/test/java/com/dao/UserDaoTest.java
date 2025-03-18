package com.dao;

import com.testRepository.TestDatabaseContainer;
import com.financeTracker.dao.UserDao;
import com.financeTracker.dao.impl.UserDaoImpl;
import com.financeTracker.model.entity.User;
import com.financeTracker.util.DatabaseManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DisplayName("Тестирование UserDao")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest {
    private UserDao userDao;

    @BeforeAll
    void setup() {
        DatabaseManager databaseManager = TestDatabaseContainer.getDatabaseManager();
        userDao = new UserDaoImpl(databaseManager.getDataSource());
    }

    private User createUniqueUser() {
        return User.builder()
                .name("testname")
                .login("login_" + UUID.randomUUID())
                .password("password")
                .build();
    }

    @Test
    @DisplayName("Сохранение пользователя")
    void save_ShouldPersistUser() {
        User uniqueUser = createUniqueUser();
        User savedUser = userDao.save(uniqueUser);

        assertNotNull(savedUser.getId(), "ID должен быть установлен после сохранения");
        assertEquals(uniqueUser.getLogin(), savedUser.getLogin(), "Логин должен совпадать");
    }

    @Test
    @DisplayName("Поиск пользователя по ID")
    void findById_ShouldReturnUser() {
        User uniqueUser = createUniqueUser();
        User savedUser = userDao.save(uniqueUser);
        Optional<User> foundUser = userDao.findById(savedUser.getId());

        assertTrue(foundUser.isPresent(), "Пользователь должен существовать");
        assertEquals(savedUser.getLogin(), foundUser.get().getLogin(), "Логин должен совпадать");
    }

    @Test
    @DisplayName("Поиск пользователя по логину")
    void findByLogin_ShouldReturnUser() {
        User uniqueUser = createUniqueUser();
        userDao.save(uniqueUser);
        Optional<User> foundUser = userDao.findByLogin(uniqueUser.getLogin());

        assertTrue(foundUser.isPresent(), "Пользователь должен быть найден по логину");
        assertEquals(uniqueUser.getLogin(), foundUser.get().getLogin(), "Логин должен совпадать");
    }

    @Test
    @DisplayName("Получение всех пользователей")
    void findAll_ShouldReturnUsers() {
        userDao.save(createUniqueUser());
        userDao.save(createUniqueUser());

        List<User> users = userDao.findAll();

        assertFalse(users.isEmpty(), "Список пользователей не должен быть пустым");
        assertTrue(users.size() >= 2, "Должно быть минимум два пользователя");
    }

    @Test
    @DisplayName("Обновление пользователя")
    void update_ShouldModifyUser() {
        User user = userDao.save(createUniqueUser());
        user.setPassword("newPassword");
        userDao.update(user);

        Optional<User> updatedUser = userDao.findById(user.getId());
        assertTrue(updatedUser.isPresent(), "Обновленный пользователь должен существовать");
        assertEquals("newPassword", updatedUser.get().getPassword(), "Пароль должен обновиться");
    }

    @Test
    @DisplayName("Удаление пользователя")
    void delete_ShouldRemoveUser() {
        User user = userDao.save(createUniqueUser());
        userDao.delete(user.getId());

        Optional<User> deletedUser = userDao.findById(user.getId());
        assertFalse(deletedUser.isPresent(), "Пользователь должен быть удален");
    }
}



