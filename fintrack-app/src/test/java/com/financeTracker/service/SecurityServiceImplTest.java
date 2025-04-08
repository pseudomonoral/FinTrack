package com.financeTracker.service;

import com.financeTracker.dao.UserDao;
import com.financeTracker.dto.AuthRequestDto;
import com.financeTracker.model.entity.User;
import com.financeTracker.service.impl.SecurityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.financeTracker.model.type.UserStatus.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private SecurityServiceImpl securityService;

    private AuthRequestDto authDto;

    @BeforeEach
    void setUp() {
        authDto = AuthRequestDto.builder()
                .login("user@example.com")
                .password("secret")
                .build();
    }

    @Test
    void registration_shouldSaveNewUserAndReturn() {
        when(userDao.findByLogin(authDto.getLogin())).thenReturn(Optional.empty());
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        User expectedUser = User.builder()
                .id(1L)
                .login("user@example.com")
                .password("secret")
                .userStatus(ACTIVE)
                .build();

        when(userDao.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        User result = securityService.registration(authDto);

        verify(userDao).save(captor.capture());
        User saved = captor.getValue();

        assertEquals("user@example.com", saved.getLogin());
        assertEquals("secret", saved.getPassword());
        assertEquals(expectedUser.getId(), result.getId());
    }

    @Test
    void authorize_shouldReturnUserIfPasswordMatchesAndStatusIsActive() {
        User existingUser = User.builder()
                .login("user@example.com")
                .password("secret")
                .userStatus(ACTIVE)
                .build();

        when(userDao.findByLogin(authDto.getLogin())).thenReturn(Optional.of(existingUser));

        User result = securityService.authorize(authDto);

        assertEquals(existingUser.getLogin(), result.getLogin());
    }
}
