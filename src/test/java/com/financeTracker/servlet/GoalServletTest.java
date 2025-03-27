package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.controller.GoalController;
import com.financeTracker.dto.GoalRequestDto;
import com.financeTracker.dto.GoalResponseDto;
import com.financeTracker.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;

@DisplayName("GoalServlet Test")
class GoalServletTest extends AbstractServletTest {

    @Mock
    private GoalController goalController;

    @Mock
    private PrintWriter writer;

    @InjectMocks
    private GoalServlet servlet;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Create a new goal, expected success")
    void doPost() throws IOException {
        UserDto user = UserDto.builder().id(1L).login("test@example.com").build();
        GoalRequestDto requestDto = new GoalRequestDto();
        GoalResponseDto responseDto = GoalResponseDto.builder().id(1L).name("Save Money").build();

        String json = objectMapper.writeValueAsString(requestDto);
        BufferedReader reader = new BufferedReader(new StringReader(json));

        Mockito.when(request.getReader()).thenReturn(reader);
        Mockito.when(session.getAttribute("user")).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(response.getWriter()).thenReturn(writer);
        Mockito.when(goalController.createGoal(user.getId(), requestDto)).thenReturn(responseDto);

        servlet.doPost(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
        Mockito.verify(writer).write(objectMapper.writeValueAsString(responseDto));
    }

    @Test
    @DisplayName("Get all goals, expected success")
    void doGetAll() throws IOException {
        UserDto user = UserDto.builder().id(1L).login("test@example.com").build();
        List<GoalResponseDto> goals = List.of(GoalResponseDto.builder().id(1L).name("Save Money").build());

        Mockito.when(session.getAttribute("user")).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(response.getWriter()).thenReturn(writer);
        Mockito.when(goalController.getGoals(user.getId())).thenReturn(goals);

        servlet.doGet(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).write(objectMapper.writeValueAsString(goals));
    }

    @Test
    @DisplayName("Update an existing goal, expected success")
    void doPut() throws IOException {
        UserDto user = UserDto.builder().id(1L).login("test@example.com").build();
        GoalRequestDto requestDto = new GoalRequestDto();
        GoalResponseDto responseDto = GoalResponseDto.builder().id(1L).name("Updated Goal").build();

        String json = objectMapper.writeValueAsString(requestDto);
        BufferedReader reader = new BufferedReader(new StringReader(json));

        Mockito.when(request.getReader()).thenReturn(reader);
        Mockito.when(session.getAttribute("user")).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(response.getWriter()).thenReturn(writer);
        Mockito.when(goalController.updateGoal(user.getId(), requestDto)).thenReturn(responseDto);

        servlet.doPut(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).write(objectMapper.writeValueAsString(responseDto));
    }

    @Test
    @DisplayName("Delete goal by name, expected success")
    void doDelete() throws IOException {
        UserDto user = UserDto.builder().id(1L).login("test@example.com").build();
        Mockito.when(request.getParameter("name")).thenReturn("Save Money");
        Mockito.when(session.getAttribute("user")).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);

        servlet.doDelete(request, response);

        Mockito.verify(goalController).deleteGoal("Save Money", user.getId());
        Mockito.verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}