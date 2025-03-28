package com.financeTracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.ApplicationContext;
import com.financeTracker.controller.TransactionController;
import com.financeTracker.dto.TransactionRequestDto;
import com.financeTracker.dto.TransactionResponseDto;
import com.financeTracker.dto.UserDto;
import com.financeTracker.model.type.TransactionType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@DisplayName("Transaction servlet test")
class TransactionServletTest extends AbstractServletTest {

    @Mock
    private TransactionController transactionController;

    @Mock
    private PrintWriter writer;

    @InjectMocks
    private TransactionServlet transactionServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ApplicationContext.setBean("transactionController", transactionController);
        transactionServlet = new TransactionServlet();
    }

    @Test
    @DisplayName("Create a transaction, expected success")
    void doPost() throws IOException {
        TransactionRequestDto requestDto = TransactionRequestDto.builder()
                .amount(100.0)
                .category("Food")
                .description("Lunch")
                .type(TransactionType.EXPENSE)
                .build();
        TransactionResponseDto responseDto = TransactionResponseDto.builder()
                .id(1L)
                .amount(100.0)
                .category("Food")
                .description("Lunch")
                .type(TransactionType.EXPENSE)
                .createdAt(LocalDateTime.now())
                .build();

        String json = new ObjectMapper().writeValueAsString(requestDto);
        BufferedReader reader = new BufferedReader(new StringReader(json));

        when(request.getReader()).thenReturn(reader);
        when(session.getAttribute("user")).thenReturn(UserDto.builder().id(1L).build());
        when(request.getSession()).thenReturn(session);
        when(transactionController.createTransaction(eq(1L), any())).thenReturn(responseDto);
        when(response.getWriter()).thenReturn(writer);

        transactionServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    @DisplayName("Update a transaction, expected success")
    void doPut() throws IOException, ServletException {
        TransactionRequestDto requestDto = TransactionRequestDto.builder()
                .amount(150.0)
                .category("Transport")
                .description("Bus ticket")
                .type(TransactionType.EXPENSE)
                .build();
        TransactionResponseDto responseDto = TransactionResponseDto.builder()
                .id(1L)
                .amount(150.0)
                .category("Transport")
                .description("Bus ticket")
                .type(TransactionType.EXPENSE)
                .createdAt(LocalDateTime.now())
                .build();

        String json = new ObjectMapper().writeValueAsString(requestDto);
        BufferedReader reader = new BufferedReader(new StringReader(json));

        when(request.getReader()).thenReturn(reader);
        when(request.getPathInfo()).thenReturn("/1");
        when(session.getAttribute("user")).thenReturn(UserDto.builder().id(1L).build());
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);
        when(transactionController.updateTransaction(eq(1L), eq(1L), any())).thenReturn(responseDto);

        transactionServlet.doPut(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Delete a transaction, expected success")
    void doDelete() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");

        transactionServlet.doDelete(request, response);

        verify(transactionController).deleteTransaction(1L);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Get all transactions, expected success")
    void doGetAll() throws IOException, ServletException {
        List<TransactionResponseDto> transactions = List.of(
                TransactionResponseDto.builder().id(1L).amount(200.0).category("Shopping").description("Clothes").type(TransactionType.EXPENSE).createdAt(LocalDateTime.now()).build()
        );

        when(session.getAttribute("user")).thenReturn(UserDto.builder().id(1L).build());
        when(request.getSession()).thenReturn(session);
        when(transactionController.getTransactions(1L)).thenReturn(transactions);
        when(response.getWriter()).thenReturn(writer);

        transactionServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}


