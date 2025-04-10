package com.financeTracker.controller;

import com.financeTracker.dto.user.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.dto.notification.NotificationDto;
import com.financeTracker.dto.user.TargetUserDto;
import com.financeTracker.model.type.NotificationType;
import com.financeTracker.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationRestController.class)
class NotificationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

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
    @DisplayName("GET /api/notification/all - should return all notifications")
    void getAllNotifications_shouldReturnOk() throws Exception {
        List<NotificationDto> notifications = List.of(
                NotificationDto.builder()
                        .createdAt(LocalDateTime.of(2025, 4, 10, 12, 0))
                        .userId(mockUser.getId())
                        .type(NotificationType.GOAL_ACHIEVED)
                        .build(),
                NotificationDto.builder()
                        .createdAt(LocalDateTime.of(2025, 4, 10, 13, 0))
                        .userId(mockUser.getId())
                        .type(NotificationType.BUDGET_EXCEEDED)
                        .build()
        );

        when(notificationService.getAllByUserId(new TargetUserDto(mockUser.getId())))
                .thenReturn(notifications);

        mockMvc.perform(get("/api/notification/all")
                        .session(getSessionWithUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].type").value("GOAL_ACHIEVED"))
                .andExpect(jsonPath("$[1].type").value("BUDGET_EXCEEDED"));
    }

    @Test
    @DisplayName("GET /api/notification/unread - should return unread notifications")
    void getUnreadNotifications_shouldReturnOk() throws Exception {
        List<NotificationDto> unread = List.of(
                NotificationDto.builder()
                        .createdAt(LocalDateTime.of(2025, 4, 10, 14, 0))
                        .userId(mockUser.getId())
                        .type(NotificationType.BUDGET_EXCEEDED)
                        .build()
        );

        when(notificationService.getUnreadByUserId(new TargetUserDto(mockUser.getId())))
                .thenReturn(unread);

        mockMvc.perform(get("/api/notification/unread")
                        .session(getSessionWithUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].type").value("BUDGET_EXCEEDED"))
                .andExpect(jsonPath("$[0].userId").value(mockUser.getId()));
    }

    @Test
    @DisplayName("DELETE /api/notification - should delete all notifications and return 204")
    void deleteAllNotifications_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/notification")
                        .session(getSessionWithUser()))
                .andExpect(status().isNoContent());

        verify(notificationService).deleteAllByUserId(new TargetUserDto(mockUser.getId()));
    }
}

