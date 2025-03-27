//package com.financeTracker.servlet;
//
//import com.financeTracker.controller.AuthController;
//import com.financeTracker.dto.AuthRequestDto;
//import com.financeTracker.dto.UserDto;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringReader;
//
//@DisplayName("SecurityServlet Test")
//class SecurityServletTest extends AbstractServletTest {
//
//    @Mock
//    private AuthController authController;
//
//    @InjectMocks
//    private SecurityServlet servlet;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Mock
//    private PrintWriter writer;
//
//    @Test
//    @DisplayName("Register user, expected success")
//    void doPostRegister() throws IOException {
//        String pathInfo = "/register";
//        AuthRequestDto authRequest = new AuthRequestDto("user", "password");
//
//        Mockito.when(request.getPathInfo()).thenReturn(pathInfo);
//        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(authRequest))));
//        Mockito.when(response.getWriter()).thenReturn(writer);
//
//        UserDto userDto = new UserDto(1L, "user", "password");
//        Mockito.when(authController.register(authRequest)).thenReturn(userDto);
//
//
//        servlet.doPost(request, response);
//
//        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
//        Mockito.verify(writer).write(objectMapper.writeValueAsString(userDto));
//    }
//
//    @Test
//    @DisplayName("Login user, expected success")
//    void doPostLogin() throws IOException {
//
//        String pathInfo = "/login";
//        AuthRequestDto authRequest = new AuthRequestDto("user", "password");
//
//        Mockito.when(request.getPathInfo()).thenReturn(pathInfo);
//        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(authRequest))));
//        Mockito.when(response.getWriter()).thenReturn(writer);
//
//
//        UserDto userDto = new UserDto(1L, "user", "password");
//        Mockito.when(authController.authorize(authRequest)).thenReturn(userDto);
//        Mockito.when(request.getSession()).thenReturn(session);
//
//
//        servlet.doPost(request, response);
//
//
//        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
//        Mockito.verify(writer).write(objectMapper.writeValueAsString(userDto));
//        Mockito.verify(session).setAttribute("user", userDto);
//    }
//
//    @Test
//    @DisplayName("Invalid path, expected 404")
//    void doPostInvalidPath() throws IOException {
//
//        String pathInfo = "/invalid-path";
//        AuthRequestDto authRequest = new AuthRequestDto("user", "password");
//
//        Mockito.when(request.getPathInfo()).thenReturn(pathInfo);
//        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(authRequest))));
//
//
//        servlet.doPost(request, response);
//
//
//        Mockito.verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Неизвестный путь");
//    }
//
//    @Test
//    @DisplayName("Empty path, expected 400")
//    void doPostEmptyPath() throws IOException {
//
//        String pathInfo = "";
//        AuthRequestDto authRequest = new AuthRequestDto("user", "password");
//
//        Mockito.when(request.getPathInfo()).thenReturn(pathInfo);
//        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(authRequest))));
//
//
//        servlet.doPost(request, response);
//
//        Mockito.verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указан путь запроса");
//    }
//
//    @Test
//    @DisplayName("Invalid request data, expected 400")
//    void doPostInvalidRequestData() throws IOException {
//
//        String pathInfo = "/register";
//        String invalidRequestData = "invalid data";
//
//        Mockito.when(request.getPathInfo()).thenReturn(pathInfo);
//        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(invalidRequestData)));
//
//
//        servlet.doPost(request, response);
//
//        Mockito.verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указан путь запроса");
//    }
//}
//
