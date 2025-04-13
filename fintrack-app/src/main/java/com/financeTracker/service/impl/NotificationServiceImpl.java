package com.financeTracker.service.impl;


import com.financeTracker.dao.NotificationDao;
import com.financeTracker.dto.notification.NotificationResponseDto;
import com.financeTracker.dto.user.TargetUserDto;
import com.financeTracker.mapper.NotificationMapper;
import com.financeTracker.model.entity.Notification;
import com.financeTracker.model.type.NotificationType;
import com.financeTracker.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDao notificationDao;
    private final NotificationMapper notificationMapper;

    @Transactional
    @Override
    public void createNotification(NotificationType notificationType,
                                   TargetUserDto targetUserDto) {
        Notification notification = Notification.builder()
                .type(notificationType)
                .userId(targetUserDto.getUserId())
                .build();
        notification.onCreate();
        notificationDao.save(notification);
    }

    @Override
    public List<NotificationResponseDto> getAllByUserId(TargetUserDto targetUserDto) {
        Long userId = targetUserDto.getUserId();
        return notificationDao.findAllByUserId(userId)
                .stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    @Override
    public List<NotificationResponseDto> getUnreadByUserId(TargetUserDto targetUserDto) {
        Long userId = targetUserDto.getUserId();
        List<Notification> unreadNotifications = notificationDao.findUnreadByUserId(userId);
        notificationDao.readAllByUserId(userId);

        return unreadNotifications.stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void deleteAllByUserId(TargetUserDto targetUserDto) {
        notificationDao.deleteAllByUserId(targetUserDto.getUserId());
    }
}
