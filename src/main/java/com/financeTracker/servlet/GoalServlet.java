package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.GoalController;
import com.financeTracker.dto.GoalCreateRequestDto;
import com.financeTracker.dto.GoalResponseDto;
import com.financeTracker.dto.GoalUpdateRequestDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@WebServlet("/goals/*")
@RequiredArgsConstructor
public class GoalServlet extends HttpServlet {

    private final GoalController goalController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        GoalCreateRequestDto requestDTO = objectMapper.readValue(req.getReader(), GoalCreateRequestDto.class);

        GoalResponseDto createdGoal = goalController.createGoal(userId, requestDTO);
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), createdGoal);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        String goalName = req.getParameter("name");

        if (goalName != null) {
            GoalResponseDto goal = goalController.getGoal(goalName, userId);
            if (goal != null) {
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getWriter(), goal);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Goal not found");
            }
        } else {
            List<GoalResponseDto> goals = goalController.getGoals(userId);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), goals);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        GoalUpdateRequestDto requestDTO = objectMapper.readValue(req.getReader(), GoalUpdateRequestDto.class);

        GoalResponseDto updatedGoal = goalController.updateGoal(userId, requestDTO);
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), updatedGoal);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        String goalName = req.getParameter("name");

        if (goalName != null) {
            goalController.deleteGoal(goalName, userId);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Goal name is required");
        }
    }
}

