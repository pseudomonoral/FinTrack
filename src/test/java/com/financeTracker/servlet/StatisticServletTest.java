package com.financeTracker.servlet;

import com.financeTracker.controller.StatisticsController;
import com.financeTracker.dto.PeriodRequestDto;
import com.financeTracker.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.time.LocalDate;

@DisplayName("StatisticServlet Test")
class StatisticServletTest extends AbstractServletTest {

    @Mock
    private StatisticsController statisticsController;

    @Mock
    private PrintWriter writer;

    @InjectMocks
    private StatisticServlet servlet;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Get current balance, expected success")
    void doGetCurrentBalance() throws IOException {
        UserDto userDto = new UserDto(1L, "user1", "password"); // Mock user DTO
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("user")).thenReturn(userDto);
        Mockito.when(response.getWriter()).thenReturn(writer);

        Long userId = userDto.getId();
        Mockito.when(statisticsController.getCurrentBalance(userId)).thenReturn(1000.0);

        servlet.doGet(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).write(objectMapper.writeValueAsString(1000.0));
    }

    @Test
    @DisplayName("Get expense analysis, expected success")
    void doGetExpenseAnalysis() throws IOException {
        UserDto userDto = new UserDto(1L, "user1", "password");
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("user")).thenReturn(userDto);
        Mockito.when(response.getWriter()).thenReturn(writer);

        Long userId = userDto.getId();
        Mockito.when(statisticsController.getExpenseAnalysis(userId)).thenReturn(new Object());

        servlet.doGet(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).write(objectMapper.writeValueAsString(new Object()));
    }

    @Test
    @DisplayName("Get goal statistics, expected success")
    void doGetGoalStatistics() throws IOException {
        UserDto userDto = new UserDto(1L, "user1", "password");
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("user")).thenReturn(userDto);
        Mockito.when(response.getWriter()).thenReturn(writer);

        Long userId = userDto.getId();
        Mockito.when(statisticsController.getGoalStatistics(userId)).thenReturn(new Object());

        servlet.doGet(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).write(objectMapper.writeValueAsString(new Object()));
    }

    @Test
    @DisplayName("Handle invalid endpoint, expected 404")
    void doGetInvalidEndpoint() throws IOException {
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("user")).thenReturn(new UserDto(1L, "user1", "password"));
        Mockito.when(request.getPathInfo()).thenReturn("/invalid-endpoint");
        Mockito.when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        Mockito.verify(writer).write("{\"error\": \"Endpoint not found\"}");
    }

    @Test
    @DisplayName("Post income for period, expected success")
    void doPostIncomeForPeriod() throws IOException {
        UserDto userDto = new UserDto(1L, "user1", "password");
        PeriodRequestDto requestDto = new PeriodRequestDto(LocalDate.now(), LocalDate.now().plusMonths(1));
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("user")).thenReturn(userDto);
        Mockito.when(request.getPathInfo()).thenReturn("/income-for-period");
        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(requestDto))));
        Mockito.when(response.getWriter()).thenReturn(writer);

        Long userId = userDto.getId();
        Mockito.when(statisticsController.getIncomeForPeriod(userId, requestDto.getStartDate(), requestDto.getEndDate())).thenReturn(500.0);

        servlet.doPost(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).write(objectMapper.writeValueAsString(500.0));
    }

    @Test
    @DisplayName("Post expense for period, expected success")
    void doPostExpenseForPeriod() throws IOException {
        UserDto userDto = new UserDto(1L, "user1", "password");
        PeriodRequestDto requestDto = new PeriodRequestDto(LocalDate.now(), LocalDate.now().plusMonths(1));
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("user")).thenReturn(userDto);
        Mockito.when(request.getPathInfo()).thenReturn("/expense-for-period");
        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(requestDto))));
        Mockito.when(response.getWriter()).thenReturn(writer);

        Long userId = userDto.getId();
        Mockito.when(statisticsController.getExpenseForPeriod(userId, requestDto.getStartDate(), requestDto.getEndDate())).thenReturn(300.0);

        servlet.doPost(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).write(objectMapper.writeValueAsString(300.0));
    }

    @Test
    @DisplayName("Handle invalid request data in POST, expected 400")
    void doPostInvalidRequestData() throws IOException {
        UserDto userDto = new UserDto(1L, "user1", "password");
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("user")).thenReturn(userDto);
        Mockito.when(request.getPathInfo()).thenReturn("/income-for-period");
        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader("invalid data")));
        Mockito.when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(writer).write("{\"error\": \"Invalid request data\"}");
    }

    @Test
    @DisplayName("Handle invalid endpoint in POST, expected 404")
    void doPostInvalidEndpoint() throws IOException {
        UserDto userDto = new UserDto(1L, "user1", "password");
        PeriodRequestDto requestDto = new PeriodRequestDto(LocalDate.now(), LocalDate.now().plusMonths(1));
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("user")).thenReturn(userDto);
        Mockito.when(request.getPathInfo()).thenReturn("/invalid-endpoint");
        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(requestDto))));
        Mockito.when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        Mockito.verify(writer).write("{\"error\": \"Endpoint not found\"}");
    }
}

