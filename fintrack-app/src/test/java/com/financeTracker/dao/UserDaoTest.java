package com.financeTracker.dao;

import com.financeTracker.dao.impl.UserDaoImpl;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.UserRole;
import com.financeTracker.model.type.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDaoTest extends AbstractDaoTest {

    private UserDaoImpl userDao;

    @BeforeEach
    void setUpDao() {
        userDao = new UserDaoImpl(jdbcTemplate);
    }


    @Test
    void saveAndFindById_shouldWorkCorrectly() {
        User user = User.builder()
                .login("john_doe")
                .password("pass123")
                .name("John Doe")
                .userRole(UserRole.USER)
                .userStatus(UserStatus.ACTIVE)
                .build();

        User saved = userDao.save(user);

        Optional<User> fromDb = userDao.findById(saved.getId());
        assertTrue(fromDb.isPresent());

        User actual = fromDb.get();
        assertEquals("john_doe", actual.getLogin());
        assertEquals("pass123", actual.getPassword());
        assertEquals("John Doe", actual.getName());
        assertEquals(UserRole.USER, actual.getUserRole());
        assertEquals(UserStatus.ACTIVE, actual.getUserStatus());
    }

    @Test
    void findByLogin_shouldReturnCorrectUser() {
        User user = User.builder()
                .login("alice")
                .password("123")
                .name("Alice")
                .userRole(UserRole.ADMIN)
                .userStatus(UserStatus.BLOCKED)
                .build();

        userDao.save(user);

        Optional<User> found = userDao.findByLogin("alice");
        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getName());
        assertEquals(UserRole.ADMIN, found.get().getUserRole());
    }

    @Test
    void findAll_shouldReturnListOfUsers() {
        User u1 = User.builder().login("u1").password("p1").name("User1")
                .userRole(UserRole.USER).userStatus(UserStatus.ACTIVE).build();

        User u2 = User.builder().login("u2").password("p2").name("User2")
                .userRole(UserRole.ADMIN).userStatus(UserStatus.BLOCKED).build();

        userDao.save(u1);
        userDao.save(u2);

        List<User> users = userDao.findAll();
        assertTrue(users.size() >= 2);
    }

    @Test
    void update_shouldModifyUserCorrectly() {
        User user = User.builder()
                .login("old")
                .password("oldpass")
                .name("Old Name")
                .userRole(UserRole.USER)
                .userStatus(UserStatus.ACTIVE)
                .build();

        User saved = userDao.save(user);

        saved.setLogin("newlogin");
        saved.setPassword("newpass");
        saved.setName("New Name");
        saved.setUserRole(UserRole.ADMIN);
        saved.setUserStatus(UserStatus.BLOCKED);

        userDao.update(saved);

        Optional<User> updated = userDao.findById(saved.getId());
        assertTrue(updated.isPresent());

        User actual = updated.get();
        assertEquals("newlogin", actual.getLogin());
        assertEquals("newpass", actual.getPassword());
        assertEquals("New Name", actual.getName());
        assertEquals(UserRole.ADMIN, actual.getUserRole());
        assertEquals(UserStatus.BLOCKED, actual.getUserStatus());
    }

    @Test
    void delete_shouldRemoveUser() {
        User user = User.builder()
                .login("tempuser")
                .password("temppass")
                .name("Temp")
                .userRole(UserRole.USER)
                .userStatus(UserStatus.ACTIVE)
                .build();

        User saved = userDao.save(user);
        userDao.delete(saved.getId());

        Optional<User> result = userDao.findById(saved.getId());
        assertTrue(result.isEmpty());
    }
}