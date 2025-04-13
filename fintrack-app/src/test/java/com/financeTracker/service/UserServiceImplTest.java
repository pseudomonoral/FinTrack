package com.financeTracker.service;

import com.financeTracker.dao.UserDao;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.dto.user.UserUpdateRequestDto;
import com.financeTracker.exceptions.entity.EntityAlreadyExistException;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.mapper.UserMapper;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.UserStatus;
import com.financeTracker.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private final Long userId = 1L;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(userId)
                .login("test@example.com")
                .password("password")
                .name("John Doe")
                .build();

        userDto = UserDto.builder()
                .id(userId)
                .login("test@example.com")
                .name("John Doe")
                .userRole("USER")
                .userStatus("ACTIVE")
                .build();
    }

    @Test
    void getUserById_shouldReturnDto_whenUserExists() {
        when(userDao.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserById(userId);

        assertEquals(userDto, result);
        verify(userDao).findById(userId);
        verify(userMapper).toUserDto(user);
    }

    @Test
    void getUserById_shouldThrowException_whenUserNotFound() {
        when(userDao.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void deleteAccountUserById_shouldDeleteWithoutCheckingExistence() {
        userService.deleteAccountUserById(userId);
        verify(userDao).delete(userId);
    }

    @Test
    void updateUser_shouldUpdate_whenValid() {
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto("Jane", "new@example.com", "newpass");

        when(userDao.findById(userId)).thenReturn(Optional.of(user));
        when(userDao.findByLogin(updateDto.getLogin())).thenReturn(Optional.empty());

        userService.updateUser(userId, updateDto);

        assertEquals("Jane", user.getName());
        assertEquals("new@example.com", user.getLogin());
        assertEquals("newpass", user.getPassword());
        verify(userDao).update(user);
    }

    @Test
    void updateUser_shouldThrow_whenUserNotFound() {
        when(userDao.findById(userId)).thenReturn(Optional.empty());

        UserUpdateRequestDto updateDto = new UserUpdateRequestDto("Jane", "new@example.com", "newpass");

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userId, updateDto));
    }

    @Test
    void updateUser_shouldThrow_whenLoginTakenByAnotherUser() {
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto("Jane", "duplicate@example.com", "newpass");
        User anotherUser = User.builder().id(2L).login("duplicate@example.com").build();

        when(userDao.findById(userId)).thenReturn(Optional.of(user));
        when(userDao.findByLogin(updateDto.getLogin())).thenReturn(Optional.of(anotherUser));

        assertThrows(EntityAlreadyExistException.class, () -> userService.updateUser(userId, updateDto));
    }

    @Test
    void blockedUserById_shouldSetBlockedStatus() {
        when(userDao.findById(userId)).thenReturn(Optional.of(user));

        userService.blockedUserById(userId);

        assertEquals(UserStatus.BLOCKED, user.getUserStatus());
        verify(userDao).update(user);
    }

    @Test
    void blockedUserById_shouldThrow_whenNotFound() {
        when(userDao.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.blockedUserById(userId));
    }

    @Test
    void unblockedUserById_shouldSetActiveStatus() {
        user.setUserStatus(UserStatus.BLOCKED);
        when(userDao.findById(userId)).thenReturn(Optional.of(user));

        userService.unblockedUserById(userId);

        assertEquals(UserStatus.ACTIVE, user.getUserStatus());
        verify(userDao).update(user);
    }

    @Test
    void unblockedUserById_shouldThrow_whenNotFound() {
        when(userDao.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.unblockedUserById(userId));
    }

    @Test
    void showAll_shouldReturnAllUsersMappedToDto() {
        List<User> users = List.of(user);
        List<UserDto> expected = List.of(userDto);

        when(userDao.findAll()).thenReturn(users);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        List<UserDto> result = userService.showAll();

        assertEquals(expected, result);
        verify(userDao).findAll();
    }
}
