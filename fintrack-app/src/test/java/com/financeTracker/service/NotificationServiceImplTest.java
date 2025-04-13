package com.financeTracker.service;

import com.financeTracker.dao.NotificationDao;
import com.financeTracker.dto.notification.NotificationResponseDto;
import com.financeTracker.dto.user.TargetUserDto;
import com.financeTracker.mapper.NotificationMapper;
import com.financeTracker.model.entity.Notification;
import com.financeTracker.model.type.NotificationType;
import com.financeTracker.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    private NotificationDao notificationDao;
    private NotificationMapper notificationMapper;
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        notificationDao = mock(NotificationDao.class);
        notificationMapper = mock(NotificationMapper.class);
        notificationService = new NotificationServiceImpl(notificationDao, notificationMapper);
    }

    @Test
    @DisplayName("createNotification should save notification with correct values")
    void createNotification_shouldSaveNotification() {
        NotificationType type = NotificationType.GOAL_ACHIEVED;
        Long userId = 1L;
        TargetUserDto dto = TargetUserDto.builder().userId(userId).build();

        notificationService.createNotification(type, dto);

        verify(notificationDao).save(argThat(notification ->
                notification.getType() == type &&
                        notification.getUserId().equals(userId) &&
                        notification.getCreatedAt() != null
        ));
    }

    @Test
    @DisplayName("getAllByUserId should return mapped list of NotificationDto")
    void getAllByUserId_shouldReturnDtos() {
        Long userId = 2L;
        TargetUserDto dto = TargetUserDto.builder().userId(userId).build();

        Notification n1 = Notification.builder().userId(userId).type(NotificationType.BUDGET_EXCEEDED).build();
        Notification n2 = Notification.builder().userId(userId).type(NotificationType.GOAL_ACHIEVED).build();

        NotificationResponseDto dto1 = NotificationResponseDto.builder().userId(userId).type(NotificationType.BUDGET_EXCEEDED).build();
        NotificationResponseDto dto2 = NotificationResponseDto.builder().userId(userId).type(NotificationType.GOAL_ACHIEVED).build();

        when(notificationDao.findAllByUserId(userId)).thenReturn(List.of(n1, n2));
        when(notificationMapper.toDto(n1)).thenReturn(dto1);
        when(notificationMapper.toDto(n2)).thenReturn(dto2);

        List<NotificationResponseDto> result = notificationService.getAllByUserId(dto);

        assertThat(result).containsExactly(dto1, dto2);
    }

    @Test
    @DisplayName("deleteAllByUserId should call DAO with correct userId")
    void deleteAllByUserId_shouldCallDao() {
        Long userId = 4L;
        TargetUserDto dto = TargetUserDto.builder().userId(userId).build();

        notificationService.deleteAllByUserId(dto);

        verify(notificationDao).deleteAllByUserId(userId);
    }
}

