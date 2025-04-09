package com.financeTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.dto.budget.BudgetCheckResponseDto;
import com.financeTracker.dto.budget.BudgetDto;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.service.BudgetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BudgetRestController.class)
class BudgetRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BudgetService budgetService;

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
    @DisplayName("POST /api/budget - should create budget and return 201")
    void createBudget_shouldReturnCreated() throws Exception {
        BudgetDto budgetDto = BudgetDto.builder().limit(1000.0).build();

        mockMvc.perform(post("/api/budget")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetDto))
                        .session(getSessionWithUser()))
                .andExpect(status().isCreated());

        verify(budgetService).createBudget(mockUser.getId(), budgetDto);
    }

    @Test
    @DisplayName("GET /api/budget - should return budget when present")
    void getBudget_shouldReturnOk() throws Exception {
        BudgetDto budgetDto = BudgetDto.builder().limit(1000.0).build();
        when(budgetService.getBudget(mockUser.getId())).thenReturn(Optional.of(budgetDto));

        mockMvc.perform(get("/api/budget")
                        .session(getSessionWithUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit").value(1000.0));
    }

    @Test
    @DisplayName("GET /api/budget - should return 404 when no budget")
    void getBudget_shouldReturnNotFound() throws Exception {
        when(budgetService.getBudget(mockUser.getId())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/budget")
                        .session(getSessionWithUser()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/budget - should update budget and return 200")
    void updateBudget_shouldReturnOk() throws Exception {
        BudgetDto budgetDto = BudgetDto.builder().limit(2000.0).build();

        mockMvc.perform(put("/api/budget")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetDto))
                        .session(getSessionWithUser()))
                .andExpect(status().isOk());

        verify(budgetService).updateBudget(mockUser.getId(), budgetDto);
    }

    @Test
    @DisplayName("DELETE /api/budget - should delete budget and return 204")
    void deleteBudget_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/budget")
                        .session(getSessionWithUser()))
                .andExpect(status().isNoContent());

        verify(budgetService).deleteBudget(mockUser.getId());
    }

    @Test
    @DisplayName("GET /api/budget/check-exceeded - should return budget check result")
    void checkExceeded_shouldReturnOk() throws Exception {
        BudgetCheckResponseDto responseDto = new BudgetCheckResponseDto(true, 1500.0, 1000.0);
        when(budgetService.isBudgetExceeded(mockUser.getId())).thenReturn(responseDto);

        mockMvc.perform(get("/api/budget/check-exceeded")
                        .session(getSessionWithUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exceeded").value(true))
                .andExpect(jsonPath("$.monthlyExpense").value(1500.0))
                .andExpect(jsonPath("$.budgetLimit").value(1000.0));
    }
}
