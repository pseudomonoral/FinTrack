package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.TransactionController;
import com.financeTracker.dto.TransactionRequestDto;
import com.financeTracker.dto.TransactionResponseDto;
import com.financeTracker.dto.UserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/transactions/*")
public class TransactionServlet extends HttpServlet {

    private final TransactionController transactionController = (TransactionController) ApplicationContext.getBean("transactionController");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        if (userDto == null) return;
        TransactionRequestDto dto = objectMapper.readValue(req.getInputStream(), TransactionRequestDto.class);
        TransactionResponseDto responseDTO = transactionController.createTransaction(userDto.getId(), dto);

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(resp.getOutputStream(), responseDTO);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long transactionId = extractTransactionId(req, resp);
        if (transactionId == null) return;

        Long userId = ApplicationContext.getAuthorizeUser().getId();
        TransactionRequestDto dto = objectMapper.readValue(req.getInputStream(), TransactionRequestDto.class);
        TransactionResponseDto responseDTO = transactionController.updateTransaction(userId, transactionId, dto);

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getOutputStream(), responseDTO);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long transactionId = extractTransactionId(req, resp);
        if (transactionId == null) return;

        transactionController.deleteTransaction(transactionId);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String category = req.getParameter("category");
        Long userId = ApplicationContext.getAuthorizeUser().getId();

        List<TransactionResponseDto> transactions;
        if (category != null) {
            transactions = transactionController.getTransactionsByCategory(userId, category);
        } else {
            transactions = transactionController.getTransactions(userId);
        }

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getOutputStream(), transactions);
    }

    private Long extractTransactionId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указан ID транзакции");
            return null;
        }
        return Long.parseLong(pathInfo.substring(1));
    }
}

