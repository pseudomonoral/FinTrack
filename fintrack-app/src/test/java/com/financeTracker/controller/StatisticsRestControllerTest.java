package com.financeTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.dto.*;
import com.financeTracker.service.StatisticsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatisticsRestController.class)
class StatisticsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService statisticsService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UserDto mockUser = UserDto.builder()
            .id(1L)
            .login("testuser")
            .name("Test User")
            .userRole("USER")
            .userStatus("ACTIVE")
            .build();

    private MockHttpSession getSessionWithUser() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", mockUser);
        return session;
    }

    @Test
    @DisplayName("GET /api/statistics/current-balance - returns balance")
    void getCurrentBalance_shouldReturnBalance() throws Exception {
        BalanceResponseDto balanceDto = BalanceResponseDto.builder().balance(5000.50).build();
        when(statisticsService.calculateCurrentBalance(mockUser.getId())).thenReturn(balanceDto);

        mockMvc.perform(get("/api/statistics/current-balance")
                        .session(getSessionWithUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(5000.50));
    }

    @Test
    @DisplayName("GET /api/statistics/expense-analysis - returns expense analysis")
    void getExpenseAnalysis_shouldReturnAnalysis() throws Exception {
        ExpenseAnalysisResponseDto dto = ExpenseAnalysisResponseDto.builder()
                .categoryExpenses(Map.of("Food", 1200.0, "Transport", 300.0))
                .build();

        when(statisticsService.analyzeExpensesByCategory(mockUser.getId())).thenReturn(dto);

        mockMvc.perform(get("/api/statistics/expense-analysis")
                        .session(getSessionWithUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryExpenses.Food").value(1200.0))
                .andExpect(jsonPath("$.categoryExpenses.Transport").value(300.0));
    }

    @Test
    @DisplayName("GET /api/statistics/goal-statistics - returns goal statistics")
    void getGoalStatistics_shouldReturnStats() throws Exception {
        GoalStatisticsResponseDto dto = GoalStatisticsResponseDto.builder()
                .completedGoals(3L)
                .averageGoalProgress(0.75)
                .build();

        when(statisticsService.analyzeGoalsByUserId(mockUser.getId())).thenReturn(dto);

        mockMvc.perform(get("/api/statistics/goal-statistics")
                        .session(getSessionWithUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completedGoals").value(3))
                .andExpect(jsonPath("$.averageGoalProgress").value(0.75));
    }

    @Test
    @DisplayName("POST /api/statistics/income-for-period - returns income")
    void getIncomeForPeriod_shouldReturnIncome() throws Exception {
        PeriodRequestDto requestDto = PeriodRequestDto.builder()
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 1, 31))
                .build();

        IncomeExpenseResponseDto responseDto = IncomeExpenseResponseDto.builder()
                .amount(8000.0)
                .build();

        when(statisticsService.calculateIncomeForPeriod(
                eq(mockUser.getId()), eq(requestDto.getStartDate()), eq(requestDto.getEndDate())))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/statistics/income-for-period")
                        .session(getSessionWithUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(8000.0));
    }

    @Test
    @DisplayName("POST /api/statistics/expense-for-period - returns expense")
    void getExpenseForPeriod_shouldReturnExpense() throws Exception {
        PeriodRequestDto requestDto = PeriodRequestDto.builder()
                .startDate(LocalDate.of(2024, 2, 1))
                .endDate(LocalDate.of(2024, 2, 28))
                .build();

        IncomeExpenseResponseDto responseDto = IncomeExpenseResponseDto.builder()
                .amount(3500.0)
                .build();

        when(statisticsService.calculateExpenseForPeriod(
                eq(mockUser.getId()), eq(requestDto.getStartDate()), eq(requestDto.getEndDate())))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/statistics/expense-for-period")
                        .session(getSessionWithUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(3500.0));
    }
}
