package com.financeTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.dto.user.UserUpdateRequestDto;
import com.financeTracker.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserSettingsRestController.class)
class UserSettingsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UserDto mockUser = UserDto.builder()
            .id(1L)
            .login("testuser@example.com")
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
    @DisplayName("GET /settings - should return current user from session")
    void getCurrentUser_shouldReturnUser() throws Exception {
        mockMvc.perform(get("/settings")
                        .session(getSessionWithUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockUser.getId()))
                .andExpect(jsonPath("$.login").value(mockUser.getLogin()));
    }

    @Test
    @DisplayName("PUT /settings - should update user and update session")
    void updateUserProfile_shouldUpdateAndSetSession() throws Exception {
        UserUpdateRequestDto updateDto = UserUpdateRequestDto.builder()
                .name("Updated Name")
                .login("newemail@example.com")
                .password("newpass")
                .build();

        UserDto updatedUser = UserDto.builder()
                .id(mockUser.getId())
                .name("Updated Name")
                .login("newemail@example.com")
                .userRole("USER")
                .userStatus("ACTIVE")
                .build();

        doNothing().when(userService).updateUser(eq(mockUser.getId()), any());
        when(userService.getUserById(mockUser.getId())).thenReturn(updatedUser);

        mockMvc.perform(put("/settings")
                        .session(getSessionWithUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /settings - should delete user and invalidate session")
    void deleteAccount_shouldDeleteAndInvalidateSession() throws Exception {
        doNothing().when(userService).deleteAccountUserById(mockUser.getId());

        mockMvc.perform(delete("/settings")
                        .session(getSessionWithUser()))
                .andExpect(status().isNoContent());
    }
}
