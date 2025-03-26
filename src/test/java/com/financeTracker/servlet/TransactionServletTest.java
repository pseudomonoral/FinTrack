package com.financeTracker.servlet;

import com.financeTracker.controller.TransactionController;
import com.financeTracker.dto.TransactionRequestDto;
import com.financeTracker.dto.TransactionResponseDto;
import com.financeTracker.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.*;
import java.util.List;

@DisplayName("Transaction Servlet Test")
class TransactionServletTest extends AbstractServletTest {

    @Mock
    TransactionController transactionController;

    @Mock
    PrintWriter writer;

    @InjectMocks
    TransactionServlet servlet;

    @Test
    @DisplayName("Create a transaction, expected success")
    void doPost() throws IOException {
        UserDto userDto = new UserDto(1, "user", "user@example.com");
        TransactionRequestDto requestDto = new TransactionRequestDto("category", 100.0);
        TransactionResponseDto responseDto = new TransactionResponseDto(1L, "category", 100.0);
        String jsonRequest = JsonUtil.writeObject(requestDto);
        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("user")).thenReturn(userDto);
        Mockito.when(request.getInputStream()).thenReturn(new ByteArrayInputStream(jsonRequest.getBytes()));
        Mockito.when(response.getWriter()).thenReturn(writer);
        Mockito.when(transactionController.createTransaction(Mockito.anyLong(), Mockito.any(TransactionRequestDto.class))).thenReturn(responseDto);

        servlet.doPost(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
        Mockito.verify(transactionController).createTransaction(Mockito.eq(userDto.getId()), Mockito.any(TransactionRequestDto.class));
        Mockito.verify(writer).write(Mockito.anyString());
    }

    @Test
    @DisplayName("Update a transaction, expected success")
    void doPut() throws IOException {
        UserDto userDto = new UserDto(1, "user", "user@example.com");
        Long transactionId = 1L;
        TransactionRequestDto requestDto = new TransactionRequestDto("category", 200.0);
        TransactionResponseDto responseDto = new TransactionResponseDto(transactionId, "category", 200.0);
        String jsonRequest = JsonUtil.writeObject(requestDto);
        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("user")).thenReturn(userDto);
        Mockito.when(request.getInputStream()).thenReturn(new ByteArrayInputStream(jsonRequest.getBytes()));
        Mockito.when(response.getWriter()).thenReturn(writer);
        Mockito.when(transactionController.updateTransaction(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(TransactionRequestDto.class))).thenReturn(responseDto);
        Mockito.when(request.getPathInfo()).thenReturn("/" + transactionId);

        servlet.doPut(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(transactionController).updateTransaction(Mockito.eq(userDto.getId()), Mockito.eq(transactionId), Mockito.any(TransactionRequestDto.class));
        Mockito.verify(writer).write(Mockito.anyString());
    }

    @Test
    @DisplayName("Delete a transaction, expected success")
    void doDelete() throws IOException {
        Long transactionId = 1L;

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getPathInfo()).thenReturn("/" + transactionId);

        servlet.doDelete(request, response);

        Mockito.verify(transactionController).deleteTransaction(transactionId);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Get all transactions, expected success")
    void doGet() throws IOException {
        UserDto userDto = new UserDto(1, "user", "user@example.com");
        List<TransactionResponseDto> transactions = List.of(
                new TransactionResponseDto(1L, "category1", 100.0),
                new TransactionResponseDto(2L, "category2", 200.0)
        );

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("user")).thenReturn(userDto);
        Mockito.when(response.getWriter()).thenReturn(writer);
        Mockito.when(request.getParameter("category")).thenReturn(null);
        Mockito.when(transactionController.getTransactions(userDto.getId())).thenReturn(transactions);

        servlet.doGet(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).write(Mockito.anyString());
    }

    @Test
    @DisplayName("Get transactions by category, expected success")
    void doGetByCategory() throws IOException {
        UserDto userDto = new UserDto(1, "user", "user@example.com");
        String category = "category1";
        List<TransactionResponseDto> transactions = List.of(
                new TransactionResponseDto(1L, category, 100.0)
        );

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("user")).thenReturn(userDto);
        Mockito.when(response.getWriter()).thenReturn(writer);
        Mockito.when(request.getParameter("category")).thenReturn(category);
        Mockito.when(transactionController.getTransactionsByCategory(userDto.getId(), category)).thenReturn(transactions);

        servlet.doGet(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).write(Mockito.anyString()); // Write the filtered list of transactions in JSON
    }
}

