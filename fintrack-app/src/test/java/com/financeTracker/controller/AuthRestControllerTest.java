package com.financeTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.dto.auth.AuthRequestDto;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.mapper.UserMapper;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.UserRole;
import com.financeTracker.model.type.UserStatus;
import com.financeTracker.service.SecurityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthRestController.class)
class AuthRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private final AuthRequestDto authRequestDto = AuthRequestDto.builder()
            .login("test@example.com")
            .password("securePass")
            .build();

    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .login("test@example.com")
            .name("Test User")
            .userRole("USER")
            .userStatus("ACTIVE")
            .build();

    private final User mockUser = User.builder()
            .id(1L)
            .login("test@example.com")
            .password("hashedPassword")
            .name("Test User")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .userRole(UserRole.USER)
            .userStatus(UserStatus.ACTIVE)
            .build();

    @Test
    @DisplayName("POST /auth/register - should register new user")
    void register_shouldReturnCreatedUser() throws Exception {
        when(securityService.registration(authRequestDto)).thenReturn(mockUser);
        when(userMapper.toUserDto(mockUser)).thenReturn(userDto);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.login").value(userDto.getLogin()));
    }

    @Test
    @DisplayName("POST /auth/login - should login user and store in session")
    void login_shouldReturnUserAndStoreInSession() throws Exception {
        when(securityService.authorize(authRequestDto)).thenReturn(mockUser);
        when(userMapper.toUserDto(mockUser)).thenReturn(userDto);

        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/auth/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(request().sessionAttribute("user", userDto));
    }
}
