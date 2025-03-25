package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.TransactionController;
import com.financeTracker.controller.UserController;
import com.financeTracker.dto.AdminActionDto;
import com.financeTracker.dto.TransactionResponseDto;
import com.financeTracker.dto.UserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {

    private final UserController userController = (UserController) ApplicationContext.getBean("userController");
    private final TransactionController transactionController = (TransactionController) ApplicationContext.getBean("transactionController");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("users".equals(action)) {
            List<UserDto> users = userController.showAllUsers();
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getWriter(), users);
        } else if ("transactions".equals(action)) {
            Long userId = Long.parseLong(req.getParameter("userId"));
            List<TransactionResponseDto> transactions = transactionController.getTransactions(userId);
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getWriter(), transactions);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");

        if ("block".equals(action)) {
            AdminActionDto adminAction = objectMapper.readValue(req.getInputStream(), AdminActionDto.class);
            userController.blockedUserById(adminAction.getUserId());
            resp.setStatus(HttpServletResponse.SC_OK);
        } else if ("unblock".equals(action)) {
            AdminActionDto adminAction = objectMapper.readValue(req.getInputStream(), AdminActionDto.class);
            userController.unblockedUserById(adminAction.getUserId());
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }
}

