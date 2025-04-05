package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.ServletContext;
import com.financeTracker.annotation.Auditable;
import com.financeTracker.annotation.Secured;
import com.financeTracker.controller.TransactionController;
import com.financeTracker.controller.UserController;
import com.financeTracker.dto.TransactionResponseDto;
import com.financeTracker.dto.UserDto;
import com.financeTracker.model.type.EventType;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {

    private final UserController userController = (UserController) ServletContext.getBean("userController");
    private final TransactionController transactionController = (TransactionController) ServletContext.getBean("transactionController");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Secured(hasRole = "ADMIN")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if ("/users".equals(path)) {
            List<UserDto> users = userController.showAllUsers();
            writeResponse(resp, users);
        } else if (path != null && path.startsWith("/transactions/")) {
            TargetUserDto targetUser = objectMapper.readValue(req.getInputStream(), TargetUserDto.class);
            List<TransactionResponseDto> transactions = transactionController.getTransactions(targetUser.getUserId());
            writeResponse(resp, transactions);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown endpoint");
        }
    }

    @Secured(hasRole = "ADMIN")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if ("/block".equals(path)) {
            TargetUserDto targetUser = objectMapper.readValue(req.getInputStream(), TargetUserDto.class);
            blockUser(req, resp, targetUser);
        } else if ("/unblock".equals(path)) {
            TargetUserDto targetUser = objectMapper.readValue(req.getInputStream(), TargetUserDto.class);
            unblockUser(req, resp, targetUser);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown endpoint");
        }
    }

    @Auditable(eventType = EventType.BLOCK_ACCOUNT)
    private void blockUser(HttpServletRequest req, HttpServletResponse resp, TargetUserDto targetUser) {
        userController.blockedUserById(targetUser.getUserId());
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Auditable(eventType = EventType.UNBLOCK_ACCOUNT)
    private void unblockUser(HttpServletRequest req, HttpServletResponse resp, TargetUserDto targetUser) {
        userController.blockedUserById(targetUser.getUserId());
        userController.unblockedUserById(targetUser.getUserId());
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void writeResponse(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getWriter(), data);
    }
}