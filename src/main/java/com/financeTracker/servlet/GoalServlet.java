package com.financeTracker.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.ServletContext;
import com.financeTracker.controller.GoalController;
import com.financeTracker.dto.GoalRequestDto;
import com.financeTracker.dto.GoalResponseDto;
import com.financeTracker.dto.UserDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/goals/*")
public class GoalServlet extends HttpServlet {

    private final GoalController goalController = (GoalController) ServletContext.getBean("goalController");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        GoalRequestDto requestDTO = objectMapper.readValue(req.getReader(), GoalRequestDto.class);

        GoalResponseDto createdGoal = goalController.createGoal(userDto.getId(), requestDTO);
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(objectMapper.writeValueAsString(createdGoal));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        String goalName = req.getParameter("name");

        if (goalName != null) {
            GoalResponseDto goal = goalController.getGoal(goalName, userDto.getId());
            if (goal != null) {
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString(goal));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Goal not found");
            }
        } else {
            List<GoalResponseDto> goals = goalController.getGoals(userDto.getId());
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(goals));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        GoalRequestDto requestDTO = objectMapper.readValue(req.getReader(), GoalRequestDto.class);

        GoalResponseDto updatedGoal = goalController.updateGoal(userDto.getId(), requestDTO);
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(objectMapper.writeValueAsString(updatedGoal));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        String goalName = req.getParameter("name");

        if (goalName != null) {
            goalController.deleteGoal(goalName, userDto.getId());
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Goal name is required");
        }
    }
}

