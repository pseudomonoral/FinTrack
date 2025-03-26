package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.ApplicationContext;
import com.financeTracker.annotation.Auditable;
import com.financeTracker.annotation.Secured;
import com.financeTracker.controller.AuthController;
import com.financeTracker.dto.AuthRequestDto;
import com.financeTracker.dto.UserDto;
import com.financeTracker.model.type.EventType;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/auth/*")
public class SecurityServlet extends HttpServlet {

    private final AuthController authController = (AuthController) ApplicationContext.getBean("authController");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указан путь запроса");
            return;
        }

        AuthRequestDto authRequest = objectMapper.readValue(req.getReader(), AuthRequestDto.class);

        switch (pathInfo) {
            case "/register":
                handleRegister(authRequest, resp);
                break;
            case "/login":
                handleLogin(authRequest, req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Неизвестный путь");
        }
    }

    @Auditable(eventType = EventType.REGISTER)
    private void handleRegister(AuthRequestDto authRequest, HttpServletResponse resp) throws IOException {
        UserDto userDto = authController.register(authRequest);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        writeResponse(resp, userDto);
    }

    @Auditable(eventType = EventType.LOGIN)
    private void handleLogin(AuthRequestDto authRequest, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto userDto = authController.authorize(authRequest);
        req.getSession().setAttribute("user", userDto);
        resp.setStatus(HttpServletResponse.SC_OK);
        writeResponse(resp, userDto);
    }

    private void writeResponse(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(objectMapper.writeValueAsString(data));
        out.flush();
    }
}


