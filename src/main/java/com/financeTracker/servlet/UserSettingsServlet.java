package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.ServletContext;
import com.financeTracker.annotation.Auditable;
import com.financeTracker.controller.UserController;
import com.financeTracker.dto.UserDto;
import com.financeTracker.dto.UserUpdateDto;
import com.financeTracker.model.type.EventType;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/settings/*")
public class UserSettingsServlet extends HttpServlet {

    private final UserController userController = (UserController) ServletContext.getBean("userController");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");

        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(userDto));
    }

    @Auditable(eventType = EventType.UPDATE_PROFILE)
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");

        UserUpdateDto updateDto = objectMapper.readValue(req.getReader(), UserUpdateDto.class);
        userController.updateUser(userDto.getId(), updateDto);

        UserDto newUserDto = userController.getUserById(userDto.getId());
        req.getSession().setAttribute("user", newUserDto);

        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Auditable(eventType = EventType.DELETE_ACCOUNT)
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");

        userController.deleteUser(userDto.getId());
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}