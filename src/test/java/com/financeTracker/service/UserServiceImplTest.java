package com.financeTracker.service;

import com.financeTracker.dao.UserDao;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.UserStatus;
import com.financeTracker.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.financeTracker.constants.TestConstants.GOAL;
import static com.financeTracker.constants.TestConstants.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Get User By Id - Should Throw Exception When Not Found")
    void getUserById_ShouldThrowException_WhenNotFound() {
        when(userDao.findById(GOAL.getUserId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(GOAL.getUserId()));
    }

    @Test
    @DisplayName("Delete User By Id - Should Delete User When Exists")
    void deleteUserById_ShouldDeleteUser_WhenExists() {
        when(userDao.findById(GOAL.getUserId())).thenReturn(Optional.of(USER));

        userService.deleteUserById(GOAL.getUserId());

        verify(userDao, times(1)).delete(USER.getId());
    }

    @Test
    @DisplayName("Delete User By Id - Should Throw Exception When Not Found")
    void deleteUserById_ShouldThrowException_WhenNotFound() {
        when(userDao.findById(GOAL.getUserId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUserById(GOAL.getUserId()));
    }

    @Test
    @DisplayName("Blocked User By Id - Should Block User When Exists")
    void blockedUserById_ShouldBlockUser_WhenExists() {
        when(userDao.findById(USER.getId())).thenReturn(Optional.of(USER));

        userService.blockedUserById(USER.getId());

        assertEquals(UserStatus.BLOCKED, USER.getUserStatus());
        verify(userDao, times(1)).update(USER);
    }

    @Test
    @DisplayName("Blocked User By Id - Should Throw Exception When Not Found")
    void blockedUserById_ShouldThrowException_WhenNotFound() {
        when(userDao.findById(USER.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.blockedUserById(USER.getId()));
    }

    @Test
    @DisplayName("Show All - Should Return User List")
    void showAll_ShouldReturnUserList() {
        when(userDao.findAll()).thenReturn(List.of(USER));

        List<User> result = userService.showAll();

        assertEquals(1, result.size());
    }
}
