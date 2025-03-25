package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.BudgetController;
import com.financeTracker.controller.StatisticsController;
import com.financeTracker.dto.BudgetCheckResponse;
import com.financeTracker.dto.BudgetCreateRequest;
import com.financeTracker.dto.BudgetResponse;
import com.financeTracker.dto.BudgetUpdateRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@WebServlet("/budget/*")
@RequiredArgsConstructor
public class BudgetServlet extends HttpServlet {

    private final BudgetController budgetController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        BudgetCreateRequest request = objectMapper.readValue(req.getInputStream(), BudgetCreateRequest.class);
        Long userId = ApplicationContext.getAuthorizeUser().getId();

        BudgetResponse response = budgetController.createBudget(userId, request);

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(resp.getOutputStream(), response);
    }

    private void handleCheckBudgetExceeded(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        StatisticsController statisticsController = (StatisticsController) ApplicationContext.getBean("statisticsController");
        double expense = statisticsController.getExpenseForPeriod(userId,
                LocalDate.now().minusMonths(1), LocalDate.now()).getAmount();

        BudgetCheckResponse response = budgetController.checkBudgetExceeded(userId, expense);

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getOutputStream(), response);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        Optional<BudgetResponse> budget = budgetController.getBudget(userId);

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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BudgetUpdateRequest request = objectMapper.readValue(req.getInputStream(), BudgetUpdateRequest.class);
        Long userId = ApplicationContext.getAuthorizeUser().getId();

        BudgetResponse response = budgetController.updateBudget(userId, request);

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getOutputStream(), response);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        budgetController.deleteBudget(userId);

        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}

