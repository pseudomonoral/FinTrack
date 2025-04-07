package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.StatisticsController;
import com.financeTracker.dto.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.Map;

@WebServlet("/statistic/*")
public class StatisticServletTest extends AbstractServletTest {

    @Mock
    private StatisticsController statisticsController;

    @Mock
    private PrintWriter writer;

    @InjectMocks
    private StatisticServlet statisticServlet;

    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ApplicationContext.setBean("statisticsController", statisticsController);
        statisticServlet = new StatisticServlet();
    }

    @Test
    @DisplayName("Get current balance, expected success")
    void doGetCurrentBalance() throws IOException {
        Mockito.when(request.getPathInfo()).thenReturn("/current-balance");
        Mockito.when(session.getAttribute("user")).thenReturn(UserDto.builder().id(1L).build());
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(response.getWriter()).thenReturn(writer);

        BalanceResponseDto balanceResponse = BalanceResponseDto.builder().balance(1000.0).build();
        Mockito.when(statisticsController.getCurrentBalance(1L)).thenReturn(balanceResponse);

        statisticServlet.doGet(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).write(objectMapper.writeValueAsString(balanceResponse));
    }

    @Test
    @DisplayName("Get expense analysis, expected success")
    void doGetExpenseAnalysis() throws IOException {
        Mockito.when(request.getPathInfo()).thenReturn("/expense-analysis");
        Mockito.when(session.getAttribute("user")).thenReturn(UserDto.builder().id(1L).build());
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(response.getWriter()).thenReturn(writer);

        ExpenseAnalysisResponseDto expenseResponse = ExpenseAnalysisResponseDto.builder()
                .categoryExpenses(Map.of("Food", 200.0, "Transport", 100.0))
                .build();
        Mockito.when(statisticsController.getExpenseAnalysis(1L)).thenReturn(expenseResponse);

        statisticServlet.doGet(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).write(objectMapper.writeValueAsString(expenseResponse));
    }

    @Test
    @DisplayName("Post income for period, expected success")
    void doPostIncomeForPeriod() throws IOException {
        Mockito.when(request.getPathInfo()).thenReturn("/income-for-period");
        Mockito.when(session.getAttribute("user")).thenReturn(UserDto.builder().id(1L).build());
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(response.getWriter()).thenReturn(writer);

        String requestBody = objectMapper.writeValueAsString(new PeriodRequestDto(LocalDate.now().minusDays(10), LocalDate.now()));
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        Mockito.when(request.getReader()).thenReturn(reader);

        IncomeExpenseResponseDto incomeResponse = IncomeExpenseResponseDto.builder().amount(5000.0).build();
        Mockito.when(statisticsController.getIncomeForPeriod(Mockito.eq(1L),
                        Mockito.any(), Mockito.any()))
                .thenReturn(incomeResponse);

        statisticServlet.doPost(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).write(objectMapper.writeValueAsString(incomeResponse));
    }
}


