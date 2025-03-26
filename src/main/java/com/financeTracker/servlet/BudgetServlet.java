package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.BudgetController;
import com.financeTracker.controller.StatisticsController;
import com.financeTracker.dto.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@WebServlet("/budget/*")
public class BudgetServlet extends HttpServlet {

    private final BudgetController budgetController = (BudgetController) ApplicationContext.getBean("budgetController");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if ("/create".equals(path)) {
            handleCreateBudget(req, resp);
        } else if ("/check-exceeded".equals(path)) {
            handleCheckBudgetExceeded(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown endpoint");
        }
    }

    private void handleCreateBudget(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        BudgetRequestDto request = objectMapper.readValue(req.getInputStream(), BudgetRequestDto.class);

        BudgetResponseDto response = budgetController.createBudget(userDto.getId(), request);

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(resp.getOutputStream(), response);
    }

    private void handleCheckBudgetExceeded(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        StatisticsController statisticsController = (StatisticsController) ApplicationContext.getBean("statisticsController");
        double expense = statisticsController.getExpenseForPeriod(userDto.getId(),
                LocalDate.now().minusMonths(1), LocalDate.now()).getAmount();

        BudgetCheckResponseDto response = budgetController.checkBudgetExceeded(userDto.getId(), expense);

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getOutputStream(), response);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        Optional<BudgetResponseDto> budget = budgetController.getBudget(userDto.getId());

        resp.setContentType("application/json");
        if (budget.isPresent()) {
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getOutputStream(), budget.get());
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Бюджет не найден\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        BudgetRequestDto request = objectMapper.readValue(req.getInputStream(), BudgetRequestDto.class);

        BudgetResponseDto response = budgetController.updateBudget(userDto.getId(), request);

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getOutputStream(), response);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        budgetController.deleteBudget(userDto.getId());

        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}