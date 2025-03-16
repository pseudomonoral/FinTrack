package com.dao;

import com.config.AbstractPostgresContainerTest;
import com.financeTracker.dao.UserDao;
import com.financeTracker.dao.impl.UserDaoImpl;
import com.financeTracker.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static com.config.TestDataSourceProvider.dataSourceFromContainer;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoTest extends AbstractPostgresContainerTest {

    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(dataSourceFromContainer());
    }

    @Test
    void testSaveAndFindById() {

        User user = User.builder()
                .name("John Doe")
                .login("testuser")
                .password("testpassword")
                .build();

        User savedUser = userDao.save(user);
        assertNotNull(savedUser.getId());

        Optional<User> retrievedUser = userDao.findById(savedUser.getId());
        assertTrue(retrievedUser.isPresent());
        assertEquals("testuser", retrievedUser.get().getLogin());
    }
}
