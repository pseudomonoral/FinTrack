package com.financeTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.dto.TargetUserDto;
import com.financeTracker.dto.TransactionResponseDto;
import com.financeTracker.dto.UserDto;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.service.TransactionService;
import com.financeTracker.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminRestController.class)
class AdminRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .login("user@example.com")
            .name("User Name")
            .userRole("ADMIN")
            .userStatus("ACTIVE")
            .build();

    private final TransactionResponseDto transactionDto = TransactionResponseDto.builder()
            .id(10L)
            .amount(100.0)
            .category("Food")
            .description("Lunch")
            .type(TransactionType.EXPENSE)
            .createdAt(LocalDateTime.now())
            .build();

    private final TargetUserDto targetUserDto = TargetUserDto.builder()
            .userId(1L)
            .build();

    @Test
    @DisplayName("GET /admin/users - should return list of users")
    void getAllUsers_shouldReturnUserList() throws Exception {
        when(userService.showAll()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userDto.getId()))
                .andExpect(jsonPath("$[0].login").value(userDto.getLogin()));
    }

    @Test
    @DisplayName("POST /admin/transactions - should return user transactions")
    void getUserTransactions_shouldReturnTransactions() throws Exception {
        when(transactionService.getTransactionsByUserId(1L)).thenReturn(List.of(transactionDto));

        mockMvc.perform(post("/admin/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(targetUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(transactionDto.getId()))
                .andExpect(jsonPath("$[0].amount").value(transactionDto.getAmount()));
    }

    @Test
    @DisplayName("POST /admin/block - should block user")
    void blockUser_shouldBlockSuccessfully() throws Exception {
        doNothing().when(userService).blockedUserById(1L);

        mockMvc.perform(post("/admin/block")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(targetUserDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /admin/unblock - should unblock user")
    void unblockUser_shouldUnblockSuccessfully() throws Exception {
        doNothing().when(userService).unblockedUserById(1L);

        mockMvc.perform(post("/admin/unblock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(targetUserDto)))
                .andExpect(status().isOk());
    }
}
