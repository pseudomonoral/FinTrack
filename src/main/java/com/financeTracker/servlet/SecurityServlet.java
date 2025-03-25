package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.controller.AuthController;
import com.financeTracker.dto.AuthRequestDto;
import com.financeTracker.dto.UserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/auth/*")
@RequiredArgsConstructor
public class SecurityServlet extends HttpServlet {

    private final AuthController authController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указан путь запроса");
            return;
        }

        AuthRequestDto authRequest = objectMapper.readValue(req.getReader(), AuthRequestDto.class);

        UserDto userDto;
        switch (pathInfo) {
            case "/register":
                userDto = authController.register(authRequest);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                break;
            case "/login":
                userDto = authController.authorize(authRequest);
                resp.setStatus(HttpServletResponse.SC_OK);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Неизвестный путь");
                return;
        }

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(objectMapper.writeValueAsString(userDto));
        out.flush();
    }
}

