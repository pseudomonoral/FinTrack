package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.UserController;
import com.financeTracker.dto.UserDto;
import com.financeTracker.dto.UserUpdateDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserSettingsServletTest extends AbstractServletTest {

    @Mock
    private UserController userController;

    @Mock
    private PrintWriter writer;

    @Mock
    private HttpSession session;

    @InjectMocks
    private UserSettingsServlet userSettingsServlet;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ApplicationContext.setBean("userController", userController);
        userSettingsServlet = new UserSettingsServlet();
    }

    @Test
    @DisplayName("Получение настроек пользователя - успешный сценарий")
    void testGetUserSettingsSuccess() throws IOException {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .login("user@example.com")
                .userRole("USER")
                .userStatus("ACTIVE")
                .build();

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(userDto);
        when(response.getWriter()).thenReturn(writer);

        userSettingsServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(writer).write(objectMapper.writeValueAsString(userDto));
    }

    @Test
    @DisplayName("Обновление настроек пользователя - успешный сценарий")
    void testUpdateUserSettingsSuccess() throws IOException {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .login("user@example.com")
                .userRole("USER")
                .userStatus("ACTIVE")
                .build();

        UserUpdateDto updateDto = UserUpdateDto.builder()
                .name("Updated Name")
                .login("updated@example.com")
                .build();

        UserDto updatedUserDto = UserDto.builder()
                .id(1L)
                .login("updated@example.com")
                .userRole("USER")
                .userStatus("ACTIVE")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(updateDto);
        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(userDto);
        when(request.getReader()).thenReturn(reader);
        when(userController.getUserById(1L)).thenReturn(updatedUserDto);

        userSettingsServlet.doPut(request, response);

        verify(userController).updateUser(eq(1L), any(UserUpdateDto.class));
        verify(session).setAttribute("user", updatedUserDto);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Удаление аккаунта пользователя - успешный сценарий")
    void testDeleteUserSuccess() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .login("user@example.com")
                .userRole("USER")
                .userStatus("ACTIVE")
                .build();

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(userDto);

        userSettingsServlet.doDelete(request, response);

        verify(userController).deleteUser(1L);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}



