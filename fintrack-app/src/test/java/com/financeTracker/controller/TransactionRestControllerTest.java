package com.financeTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.dto.transaction.TransactionRequestDto;
import com.financeTracker.dto.transaction.TransactionResponseDto;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionRestController.class)
class TransactionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

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
    @DisplayName("POST /api/transactions - create transaction")
    void createTransaction_shouldReturnCreatedTransaction() throws Exception {
        TransactionRequestDto requestDto = TransactionRequestDto.builder()
                .amount(1500.0)
                .category("Food")
                .description("Lunch")
                .type(TransactionType.EXPENSE)
                .build();

        TransactionResponseDto responseDto = TransactionResponseDto.builder()
                .id(1L)
                .amount(1500.0)
                .category("Food")
                .description("Lunch")
                .type(TransactionType.EXPENSE)
                .createdAt(LocalDateTime.now())
                .build();

        when(transactionService.createTransaction(eq(mockUser.getId()), any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/transactions")
                        .session(getSessionWithUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.category").value("Food"))
                .andExpect(jsonPath("$.amount").value(1500.0));
    }

    @Test
    @DisplayName("PUT /api/transactions/{id} - update transaction")
    void updateTransaction_shouldReturnOk() throws Exception {
        TransactionRequestDto requestDto = TransactionRequestDto.builder()
                .amount(2000.0)
                .category("Transport")
                .description("Taxi")
                .type(TransactionType.EXPENSE)
                .build();

        doNothing().when(transactionService).updateTransaction(eq(mockUser.getId()), eq(1L), any());

        mockMvc.perform(put("/api/transactions/1")
                        .session(getSessionWithUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/transactions - get all transactions")
    void getAllTransactions_shouldReturnList() throws Exception {
        List<TransactionResponseDto> list = List.of(
                TransactionResponseDto.builder().id(1L).amount(100).category("Food")
                        .type(TransactionType.EXPENSE).description("Dinner")
                        .createdAt(LocalDateTime.now()).build()
        );

        when(transactionService.getTransactionsByUserId(mockUser.getId())).thenReturn(list);

        mockMvc.perform(get("/api/transactions")
                        .session(getSessionWithUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].category").value("Food"));
    }

    @Test
    @DisplayName("GET /api/transactions?category=Food - get by category")
    void getByCategory_shouldReturnFiltered() throws Exception {
        List<TransactionResponseDto> list = List.of(
                TransactionResponseDto.builder().id(2L).amount(300).category("Food")
                        .type(TransactionType.EXPENSE).description("Lunch")
                        .createdAt(LocalDateTime.now()).build()
        );

        when(transactionService.getTransactionsByCategory(mockUser.getId(), "Food")).thenReturn(list);

        mockMvc.perform(get("/api/transactions")
                        .param("category", "Food")
                        .session(getSessionWithUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Food"));
    }

    @Test
    @DisplayName("GET /api/transactions?type=income - get by type")
    void getByType_shouldReturnFiltered() throws Exception {
        List<TransactionResponseDto> list = List.of(
                TransactionResponseDto.builder().id(3L).amount(5000).category("Salary")
                        .type(TransactionType.INCOME).description("March salary")
                        .createdAt(LocalDateTime.now()).build()
        );

        when(transactionService.getTransactionsByType(mockUser.getId(), "INCOME")).thenReturn(list);

        mockMvc.perform(get("/api/transactions")
                        .param("type", "income")
                        .session(getSessionWithUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("INCOME"))
                .andExpect(jsonPath("$[0].category").value("Salary"));
    }
}
