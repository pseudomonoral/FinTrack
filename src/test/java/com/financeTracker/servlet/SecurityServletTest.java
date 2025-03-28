package com.financeTracker.servlet;

import com.financeTracker.ApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.controller.AuthController;
import com.financeTracker.dto.AuthRequestDto;
import com.financeTracker.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityServletTest {

    @Mock
    private AuthController authController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private PrintWriter writer;

    @InjectMocks
    private SecurityServlet securityServlet;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ApplicationContext.setBean("authController", authController);
        securityServlet = new SecurityServlet();
    }


    @Test
    @DisplayName("Регистрация нового пользователя - успешный сценарий")
    void testRegisterSuccess() throws IOException {
        AuthRequestDto authRequest = new AuthRequestDto("user128@example.com",
                "password");

        UserDto userDto = UserDto.builder()
                .id(1L)
                .login("user128@example.com")
                .userRole("USER")
                .userStatus("ACTIVE")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(authRequest);
        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));

        when(request.getPathInfo()).thenReturn("/register");
        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(writer);
        when(authController.register(any(AuthRequestDto.class))).thenReturn(userDto);

        securityServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(writer).write(objectMapper.writeValueAsString(userDto));
    }

    @Test
    @DisplayName("Аутентификация пользователя - успешный сценарий")
    void testLoginSuccess() throws IOException {
        AuthRequestDto authRequest = new AuthRequestDto("user", "password");
        UserDto userDto = UserDto.builder()
                .id(1L)
                .login("user128@example.com")
                .userRole("USER")
                .userStatus("ACTIVE")
                .build();
        String jsonRequest = objectMapper.writeValueAsString(authRequest);
        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));

        when(request.getPathInfo()).thenReturn("/login");
        when(request.getReader()).thenReturn(reader);
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);
        when(authController.authorize(any(AuthRequestDto.class))).thenReturn(userDto);

        securityServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(session).setAttribute("user", userDto);
        verify(writer).write(objectMapper.writeValueAsString(userDto));
    }

    @Test
    @DisplayName("Пустой путь - ошибка 400")
    void testEmptyPath() throws IOException {
        when(request.getPathInfo()).thenReturn(null);

        securityServlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указан путь запроса");
    }
}
