package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.StatisticsController;
import com.financeTracker.dto.PeriodRequestDto;
import com.financeTracker.dto.UserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet("/statistic/*")
public class StatisticServlet extends HttpServlet {
    private final StatisticsController statisticsController = (StatisticsController) ApplicationContext.getBean("statisticsController");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        Long userId = userDto.getId();
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        try {
            switch (path) {
                case "/current-balance":
                    writer.write(objectMapper.writeValueAsString(statisticsController.getCurrentBalance(userId)));
                    resp.setStatus(HttpServletResponse.SC_OK);
                    break;
                case "/expense-analysis":
                    writer.write(objectMapper.writeValueAsString(statisticsController.getExpenseAnalysis(userId)));
                    resp.setStatus(HttpServletResponse.SC_OK);
                    break;
                case "/goal-statistics":
                    writer.write(objectMapper.writeValueAsString(statisticsController.getGoalStatistics(userId)));
                    resp.setStatus(HttpServletResponse.SC_OK);
                    break;
                default:
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    writer.write("{\"error\": \"Endpoint not found\"}");
                    break;
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writer.write("{\"error\": \"Internal server error\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        if (userDto == null) return;
        Long userId = userDto.getId();
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        String requestBody = req.getReader().lines().collect(Collectors.joining());

        try {
            switch (path) {
                case "/income-for-period": {
                    PeriodRequestDto requestDto = objectMapper.readValue(requestBody, PeriodRequestDto.class);
                    writer.write(objectMapper.writeValueAsString(
                            statisticsController.getIncomeForPeriod(userId, requestDto.getStartDate(), requestDto.getEndDate())));
                    resp.setStatus(HttpServletResponse.SC_OK);
                    break;
                }
                case "/expense-for-period": {
                    PeriodRequestDto requestDto = objectMapper.readValue(requestBody, PeriodRequestDto.class);
                    writer.write(objectMapper.writeValueAsString(
                            statisticsController.getExpenseForPeriod(userId, requestDto.getStartDate(), requestDto.getEndDate())));
                    resp.setStatus(HttpServletResponse.SC_OK);
                    break;
                }
                default:
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    writer.write("{\"error\": \"Endpoint not found\"}");
                    break;
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.write("{\"error\": \"Invalid request data\"}");
        }
    }
}

