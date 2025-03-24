package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.controller.TransactionController;
import com.financeTracker.controller.UserController;
import com.financeTracker.dto.AdminActionDTO;
import com.financeTracker.dto.TransactionResponseDTO;
import com.financeTracker.dto.UserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin")
@RequiredArgsConstructor
public class AdminServlet extends HttpServlet {

    private final UserController userController;
    private final TransactionController transactionController;
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
            List<TransactionResponseDTO> transactions = transactionController.getTransactions(userId);
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
            AdminActionDTO adminAction = objectMapper.readValue(req.getInputStream(), AdminActionDTO.class);
            userController.blockedUserById(adminAction.getUserId());
            resp.setStatus(HttpServletResponse.SC_OK);
        } else if ("unblock".equals(action)) {
            AdminActionDTO adminAction = objectMapper.readValue(req.getInputStream(), AdminActionDTO.class);
            userController.unblockedUserById(adminAction.getUserId());
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }
}

