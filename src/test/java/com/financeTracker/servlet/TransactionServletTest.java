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

        Mockito.when(request.getReader()).thenReturn(reader);
        Mockito.when(session.getAttribute("user")).thenReturn(UserDto.builder().id(1L).build());
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(transactionController.createTransaction(Mockito.eq(1L), Mockito.any())).thenReturn(responseDto);
        Mockito.when(response.getWriter()).thenReturn(writer);

        transactionServlet.doPost(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }



    @Test
    @DisplayName("Delete a transaction, expected success")
    void doDelete() throws ServletException, IOException {
        Mockito.when(request.getPathInfo()).thenReturn("/1");

        transactionServlet.doDelete(request, response);

        Mockito.verify(transactionController).deleteTransaction(1L);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Get all transactions, expected success")
    void doGetAll() throws IOException, ServletException {
        List<TransactionResponseDto> transactions = List.of(
                TransactionResponseDto.builder().id(1L).amount(200.0).category("Shopping").description("Clothes").type(TransactionType.EXPENSE).createdAt(LocalDateTime.now()).build()
        );

        Mockito.when(session.getAttribute("user")).thenReturn(UserDto.builder().id(1L).build());
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(transactionController.getTransactions(1L)).thenReturn(transactions);
        Mockito.when(response.getWriter()).thenReturn(writer);

        transactionServlet.doGet(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}


