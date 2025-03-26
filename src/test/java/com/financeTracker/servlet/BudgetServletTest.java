package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.controller.BudgetController;
import com.financeTracker.controller.StatisticsController;
import com.financeTracker.dto.BudgetCheckResponseDto;
import com.financeTracker.dto.BudgetRequestDto;
import com.financeTracker.dto.BudgetResponseDto;
import com.financeTracker.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("BudgetServlet Test")
class BudgetServletTest extends AbstractServletTest {

    @Mock
    private BudgetController budgetController;

    @Mock
    private StatisticsController statisticsController;

    @InjectMocks
    private BudgetServlet servlet;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PrintWriter writer;

    @Test
    @DisplayName("Create budget, expected success")
    void doPostCreateBudget() throws IOException {
        String requestBody = "{\"amount\": 1000, \"category\": \"Food\"}";
        BudgetRequestDto budgetRequestDto = objectMapper.readValue(requestBody, BudgetRequestDto.class);
        UserDto userDto = new UserDto(1L, "user", "password");

        when(request.getSession().getAttribute("user")).thenReturn(userDto);
        when(request.getInputStream()).thenReturn(new BufferedReader(new StringReader(requestBody)));
        when(response.getWriter()).thenReturn(writer);

        BudgetResponseDto createdBudget = new BudgetResponseDto("Food", 1000);
        when(budgetController.createBudget(userDto.getId(), budgetRequestDto)).thenReturn(createdBudget);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(writer).write(objectMapper.writeValueAsString(createdBudget));
    }

    @Test
    @DisplayName("Check if budget exceeded, expected success")
    void doPostCheckBudgetExceeded() throws IOException {
        UserDto userDto = new UserDto(1L, "user", "password");
        when(request.getSession().getAttribute("user")).thenReturn(userDto);

        double expense = 1200;
        when(statisticsController.getExpenseForPeriod(userDto.getId(), any(), any())).thenReturn(new Expense(expense));

        BudgetCheckResponseDto budgetCheckResponse = new BudgetCheckResponseDto(true);
        when(budgetController.checkBudgetExceeded(userDto.getId(), expense)).thenReturn(budgetCheckResponse);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write(objectMapper.writeValueAsString(budgetCheckResponse));
    }

    @Test
    @DisplayName("Get budget, expected success")
    void doGetBudget() throws IOException {
        UserDto userDto = new UserDto(1L, "user", "password");
        when(request.getSession().getAttribute("user")).thenReturn(userDto);

        BudgetResponseDto budget = new BudgetResponseDto("Food", 1000);
        when(budgetController.getBudget(userDto.getId())).thenReturn(Optional.of(budget));
        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write(objectMapper.writeValueAsString(budget));
    }

    @Test
    @DisplayName("Get budget, not found")
    void doGetBudgetNotFound() throws IOException {
        UserDto userDto = new UserDto(1L, "user", "password");
        when(request.getSession().getAttribute("user")).thenReturn(userDto);
        when(budgetController.getBudget(userDto.getId())).thenReturn(Optional.empty());

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(response.getWriter()).write("{\"error\": \"Бюджет не найден\"}");
    }

    @Test
    @DisplayName("Update budget, expected success")
    void doPutUpdateBudget() throws IOException {
        String requestBody = "{\"amount\": 1200, \"category\": \"Food\"}";
        BudgetRequestDto budgetRequestDto = objectMapper.readValue(requestBody, BudgetRequestDto.class);
        UserDto userDto = new UserDto(1L, "user", "password");

        when(request.getSession().getAttribute("user")).thenReturn(userDto);
        when(request.getInputStream()).thenReturn(new BufferedReader(new StringReader(requestBody)));
        when(response.getWriter()).thenReturn(writer);

        BudgetResponseDto updatedBudget = new BudgetResponseDto("Food", 1200);
        when(budgetController.updateBudget(userDto.getId(), budgetRequestDto)).thenReturn(updatedBudget);

        servlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write(objectMapper.writeValueAsString(updatedBudget));
    }

    @Test
    @DisplayName("Delete budget, expected success")
    void doDeleteBudget() {
        UserDto userDto = new UserDto(1L, "user", "password");
        when(request.getSession().getAttribute("user")).thenReturn(userDto);

        servlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}

