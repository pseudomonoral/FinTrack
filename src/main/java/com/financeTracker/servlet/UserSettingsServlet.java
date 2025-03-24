package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.controller.UserController;
import com.financeTracker.dto.UserDeleteDto;
import com.financeTracker.dto.UserDto;
import com.financeTracker.dto.UserUpdateDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@WebServlet("/api/user/settings")
@RequiredArgsConstructor
public class UserSettingsServlet extends HttpServlet {

    private final UserController userController;
    private final ObjectMapper objectMapper;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");

        if (userId == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не авторизован");
            return;
        }

        UserDto userDto = userController.getUserById(userId);
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), userDto);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");

        if (userId == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не авторизован");
            return;
        }

        UserUpdateDto updateDto = objectMapper.readValue(req.getInputStream(), UserUpdateDto.class);
        userController.updateUser(userId, updateDto);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");

        if (userId == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не авторизован");
            return;
        }

        UserDeleteDto deleteDto = objectMapper.readValue(req.getInputStream(), UserDeleteDto.class);
        userController.deleteUser(userId, deleteDto.isConfirmed());

        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}

