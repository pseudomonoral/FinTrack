package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.controller.UserController;
import com.financeTracker.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.*;

@DisplayName("UserSettingsServlet Test")
class UserSettingsServletTest extends AbstractServletTest {

    @Mock
    private UserController userController;

    @Mock
    private PrintWriter writer;

    @InjectMocks
    private UserSettingsServlet servlet;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Long USER_ID = 1L;
    private static final UserDto USER_DTO = UserDto.builder()
            .id(USER_ID)
            .login("user@example.com")
            .userRole("USER")
            .userStatus("ACTIVE")
            .build();


    @Test
    @DisplayName("Delete user account, expected success")
    void doDelete_DeleteUser_Success() {
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("user")).thenReturn(USER_DTO);

        servlet.doDelete(request, response);

        Mockito.verify(userController).deleteUser(USER_ID);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}



