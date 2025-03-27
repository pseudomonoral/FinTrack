//package com.financeTracker.servlet;
//
//import com.financeTracker.controller.UserController;
//import com.financeTracker.dto.UserDto;
//import com.financeTracker.dto.UserUpdateDto;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//
//import java.io.*;
//
//@DisplayName("User Settings Servlet Test")
//class UserSettingsServletTest extends AbstractServletTest {
//
//    @Mock
//    UserController userController;
//
//    @Mock
//    PrintWriter writer;
//
//    @InjectMocks
//    UserSettingsServlet servlet;
//
//    @Test
//    @DisplayName("Get user settings, expected success")
//    void doGet() throws IOException {
//        UserDto userDto = new UserDto(1, "user", "user@example.com");
//
//        Mockito.when(request.getSession()).thenReturn(session);
//        Mockito.when(session.getAttribute("user")).thenReturn(userDto);
//        Mockito.when(response.getWriter()).thenReturn(writer);
//
//        servlet.doGet(request, response);
//
//        Mockito.verify(response).setContentType("application/json");
//        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
//        Mockito.verify(writer).write(Mockito.anyString()); // Вывод объекта UserDto в JSON
//    }
//
//    @Test
//    @DisplayName("Update user settings, expected success")
//    void doPut() throws IOException {
//        UserDto userDto = new UserDto(1, "user", "user@example.com");
//        UserUpdateDto updateDto = new UserUpdateDto("newUser", "newuser@example.com");
//        String json = JsonUtil.writeObject(updateDto);
//        BufferedReader reader = new BufferedReader(new StringReader(json));
//
//        Mockito.when(request.getSession()).thenReturn(session);
//        Mockito.when(session.getAttribute("user")).thenReturn(userDto);
//        Mockito.when(request.getInputStream()).thenReturn(new ByteArrayInputStream(json.getBytes()));
//        Mockito.when(response.getWriter()).thenReturn(writer);
//
//        servlet.doPut(request, response);
//
//        Mockito.verify(userController).updateUser(Mockito.eq(userDto.getId()), Mockito.any(UserUpdateDto.class));
//        Mockito.verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
//        Mockito.verify(session).setAttribute("user", Mockito.any(UserDto.class));
//    }
//
//    @Test
//    @DisplayName("Delete user account, expected success")
//    void doDelete() {
//        UserDto userDto = new UserDto(1, "user", "user@example.com");
//
//        Mockito.when(request.getSession()).thenReturn(session);
//        Mockito.when(session.getAttribute("user")).thenReturn(userDto);
//
//        servlet.doDelete(request, response);
//
//        Mockito.verify(userController).deleteUser(userDto.getId());
//        Mockito.verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
//    }
//}
//
