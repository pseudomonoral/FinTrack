package com.financeTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.dto.GoalRequestDto;
import com.financeTracker.dto.GoalResponseDto;
import com.financeTracker.dto.UserDto;
import com.financeTracker.service.GoalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GoalRestController.class)
class GoalRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoalService goalService;

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
    @DisplayName("POST /api/goals - should create goal and return 201 with body")
    void createGoal_shouldReturnCreated() throws Exception {
        GoalRequestDto request = GoalRequestDto.builder()
                .name("Квартира")
                .targetAmount(1_000_000)
                .currentAmount(250_000)
                .goalActive(true)
                .build();

        GoalResponseDto response = GoalResponseDto.builder()
                .id(1L)
                .name("Квартира")
                .targetAmount(1_000_000)
                .currentAmount(250_000)
                .goalActual(true)
                .progress(0.25)
                .build();

        when(goalService.createGoal(eq(mockUser.getId()), any(GoalRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .session(getSessionWithUser()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Квартира"))
                .andExpect(jsonPath("$.targetAmount").value(1_000_000))
                .andExpect(jsonPath("$.currentAmount").value(250_000))
                .andExpect(jsonPath("$.goalActual").value(true))
                .andExpect(jsonPath("$.progress").value(0.25));
    }

    @Test
    @DisplayName("GET /api/goals - should return all goals when no name param")
    void getGoals_shouldReturnAllGoals() throws Exception {
        List<GoalResponseDto> goals = List.of(
                GoalResponseDto.builder().id(1L).name("Цель 1").targetAmount(1000).currentAmount(500).goalActual(true).progress(0.5).build(),
                GoalResponseDto.builder().id(2L).name("Цель 2").targetAmount(2000).currentAmount(1000).goalActual(true).progress(0.5).build()
        );

        when(goalService.getAllGoals(mockUser.getId())).thenReturn(goals);

        mockMvc.perform(get("/api/goals")
                        .session(getSessionWithUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Цель 1"))
                .andExpect(jsonPath("$[1].name").value("Цель 2"));
    }

    @Test
    @DisplayName("GET /api/goals?name=X - should return specific goal")
    void getGoals_withName_shouldReturnSpecificGoal() throws Exception {
        GoalResponseDto goal = GoalResponseDto.builder()
                .id(1L)
                .name("Квартира")
                .targetAmount(1000000)
                .currentAmount(250000)
                .goalActual(true)
                .progress(0.25)
                .build();

        when(goalService.getGoal(any(GoalResponseDto.class), eq(mockUser.getId()))).thenReturn(goal);

        mockMvc.perform(get("/api/goals")
                        .param("name", "Квартира")
                        .session(getSessionWithUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Квартира"))
                .andExpect(jsonPath("$.progress").value(0.25));
    }

    @Test
    @DisplayName("PUT /api/goals - should update goal and return 200")
    void updateGoal_shouldReturnOk() throws Exception {
        GoalRequestDto request = GoalRequestDto.builder()
                .name("Квартира")
                .targetAmount(1000000)
                .currentAmount(300000)
                .goalActive(true)
                .build();

        mockMvc.perform(put("/api/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .session(getSessionWithUser()))
                .andExpect(status().isOk());

        verify(goalService).updateGoal(mockUser.getId(), request);
    }

    @Test
    @DisplayName("DELETE /api/goals - should delete goal and return 204")
    void deleteGoal_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/goals")
                        .param("name", "Квартира")
                        .session(getSessionWithUser()))
                .andExpect(status().isNoContent());

        verify(goalService).deleteGoal(GoalRequestDto.builder().name("Квартира").build(), mockUser.getId());
    }
}
