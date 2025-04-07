package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.BudgetController;
import com.financeTracker.dto.BudgetDto;
import com.financeTracker.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServletTest extends AbstractServletTest{

    @Mock
    private BudgetController budgetController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private BudgetServlet budgetServlet;

    private final UserDto userDto = UserDto.builder().id(1L).login("test@example.com").build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ApplicationContext.setBean("budgetController", budgetController);
        budgetServlet = new BudgetServlet();

    }

    @Test
    @DisplayName("Создание бюджета — успех")
    void testCreateBudget() throws IOException {
        String json = objectMapper.writeValueAsString(BudgetDto.builder().limit(500.0).build());
        BufferedReader reader = new BufferedReader(new StringReader(json));

        when(request.getPathInfo()).thenReturn("/create");
        when(request.getReader()).thenReturn(reader);
        when(session.getAttribute("user")).thenReturn(userDto);
        when(request.getSession()).thenReturn(session);
        when(request.getReader()).thenReturn(reader);

        budgetServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    @DisplayName("Получение бюджета — найден")
    void testGetBudget() throws IOException {
        BudgetDto budgetDto = BudgetDto.builder().limit(1000.0).build();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(userDto);
        when(response.getWriter()).thenReturn(writer);
        when(budgetController.getBudget(1L)).thenReturn(Optional.of(budgetDto));

        budgetServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        writer.flush();
        assert stringWriter.toString().contains("1000.0");
    }

    @Test
    @DisplayName("Обновление бюджета — успех")
    void testUpdateBudget() throws IOException {
        String json = objectMapper.writeValueAsString(BudgetDto.builder().limit(750.0).build());
        BufferedReader reader = new BufferedReader(new StringReader(json));

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(userDto);
        when(request.getReader()).thenReturn(reader);

        budgetServlet.doPut(request, response);

        verify(budgetController).updateBudget(eq(1L), any(BudgetDto.class));
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Удаление бюджета — успех")
    void testDeleteBudget() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(userDto);

        budgetServlet.doDelete(request, response);

        verify(budgetController).deleteBudget(1L);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}


