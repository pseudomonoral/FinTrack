package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.financeTracker.ServletContext;
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

    private final TransactionController transactionController = (TransactionController) ServletContext.getBean("transactionController");
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .registerModule(new JavaTimeModule());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        TransactionRequestDto dto = objectMapper.readValue(req.getReader(), TransactionRequestDto.class);
        TransactionResponseDto responseDTO = transactionController.createTransaction(userDto.getId(), dto);

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), responseDTO);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long transactionId = extractTransactionId(req, resp);
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");

        TransactionRequestDto dto = objectMapper.readValue(req.getReader(), TransactionRequestDto.class);
        TransactionResponseDto responseDto = transactionController.updateTransaction(userDto.getId(), transactionId, dto);

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), responseDto);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long transactionId = extractTransactionId(req, resp);

        transactionController.deleteTransaction(transactionId);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        Long userId = userDto.getId();
        String category = req.getParameter("category");

        List<TransactionResponseDto> transactions;
        if (category != null) {
            transactions = transactionController.getTransactionsByCategory(userId, category);
        } else {
            transactions = transactionController.getTransactions(userId);
        }

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), transactions);
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

