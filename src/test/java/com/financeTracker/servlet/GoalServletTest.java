//package com.financeTracker.servlet;
//
//import com.financeTracker.controller.GoalController;
//import com.financeTracker.dto.GoalRequestDto;
//import com.financeTracker.dto.GoalResponseDto;
//import com.financeTracker.dto.UserDto;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringReader;
//import java.util.List;
//
//@DisplayName("GoalServlet Test")
//class GoalServletTest extends AbstractServletTest {
//
//    @Mock
//    private GoalController goalController;
//
//    @InjectMocks
//    private GoalServlet servlet;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Mock
//    private PrintWriter writer;
//
//    @Test
//    @DisplayName("Create goal, expected success")
//    void doPostCreateGoal() throws IOException {
//        // Mock request and response
//        String requestBody = "{\"name\": \"Goal1\", \"targetAmount\": 1000}";
//        GoalRequestDto goalRequestDto = objectMapper.readValue(requestBody, GoalRequestDto.class);
//
//        UserDto userDto = new UserDto(1L, "user", "password");
//        Mockito.when(request.getSession().getAttribute("user")).thenReturn(userDto);
//        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(requestBody)));
//        Mockito.when(response.getWriter()).thenReturn(writer);
//
//        GoalResponseDto createdGoal = new GoalResponseDto("Goal1", 1000);
//        Mockito.when(goalController.createGoal(userDto.getId(), goalRequestDto)).thenReturn(createdGoal);
//
//        servlet.doPost(request, response);
//
//        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
//        Mockito.verify(writer).write(objectMapper.writeValueAsString(createdGoal));
//    }
//
//    @Test
//    @DisplayName("Get goal by name, expected success")
//    void doGetGoalByName() throws IOException {
//        String goalName = "Goal1";
//        UserDto userDto = new UserDto(1L, "user", "password");
//        Mockito.when(request.getSession().getAttribute("user")).thenReturn(userDto);
//        Mockito.when(request.getParameter("name")).thenReturn(goalName);
//        Mockito.when(response.getWriter()).thenReturn(writer);
//
//        GoalResponseDto goal = new GoalResponseDto("Goal1", 1000);
//        Mockito.when(goalController.getGoal(goalName, userDto.getId())).thenReturn(goal);
//
//        servlet.doGet(request, response);
//
//        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
//        Mockito.verify(writer).write(objectMapper.writeValueAsString(goal));
//    }
//
//    @Test
//    @DisplayName("Get goal by name, goal not found")
//    void doGetGoalNotFound() throws IOException {
//        String goalName = "Goal1";
//        UserDto userDto = new UserDto(1L, "user", "password");
//        Mockito.when(request.getSession().getAttribute("user")).thenReturn(userDto);
//        Mockito.when(request.getParameter("name")).thenReturn(goalName);
//        Mockito.when(response.getWriter()).thenReturn(writer);
//
//        Mockito.when(goalController.getGoal(goalName, userDto.getId())).thenReturn(null);
//
//        servlet.doGet(request, response);
//
//        Mockito.verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
//        Mockito.verify(writer).write("Goal not found");
//    }
//
//    @Test
//    @DisplayName("Get all goals, expected success")
//    void doGetAllGoals() throws IOException {
//        UserDto userDto = new UserDto(1L, "user", "password");
//        Mockito.when(request.getSession().getAttribute("user")).thenReturn(userDto);
//        Mockito.when(response.getWriter()).thenReturn(writer);
//
//        List<GoalResponseDto> goals = List.of(new GoalResponseDto("Goal1", 1000));
//        Mockito.when(goalController.getGoals(userDto.getId())).thenReturn(goals);
//
//        servlet.doGet(request, response);
//
//        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
//        Mockito.verify(writer).write(objectMapper.writeValueAsString(goals));
//    }
//
//    @Test
//    @DisplayName("Update goal, expected success")
//    void doPutUpdateGoal() throws IOException {
//        String requestBody = "{\"name\": \"Goal1\", \"targetAmount\": 1500}";
//        GoalRequestDto goalRequestDto = objectMapper.readValue(requestBody, GoalRequestDto.class);
//
//        UserDto userDto = new UserDto(1L, "user", "password");
//        Mockito.when(request.getSession().getAttribute("user")).thenReturn(userDto);
//        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(requestBody)));
//        Mockito.when(response.getWriter()).thenReturn(writer);
//
//        GoalResponseDto updatedGoal = new GoalResponseDto("Goal1", 1500);
//        Mockito.when(goalController.updateGoal(userDto.getId(), goalRequestDto)).thenReturn(updatedGoal);
//
//        servlet.doPut(request, response);
//
//        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
//        Mockito.verify(writer).write(objectMapper.writeValueAsString(updatedGoal));
//    }
//
//    @Test
//    @DisplayName("Delete goal, expected success")
//    void doDeleteGoal() throws IOException {
//        String goalName = "Goal1";
//        UserDto userDto = new UserDto(1L, "user", "password");
//        Mockito.when(request.getSession().getAttribute("user")).thenReturn(userDto);
//        Mockito.when(request.getParameter("name")).thenReturn(goalName);
//
//        servlet.doDelete(request, response);
//
//        Mockito.verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
//    }
//
//    @Test
//    @DisplayName("Delete goal, goal name is required")
//    void doDeleteGoalNameRequired() throws IOException {
//        UserDto userDto = new UserDto(1L, "user", "password");
//        Mockito.when(request.getSession().getAttribute("user")).thenReturn(userDto);
//        Mockito.when(request.getParameter("name")).thenReturn(null);
//
//
//        servlet.doDelete(request, response);
//
//        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        Mockito.verify(response.getWriter()).write("Goal name is required");
//    }
//}
//
