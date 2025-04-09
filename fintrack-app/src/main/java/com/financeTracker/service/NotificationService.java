package com.financeTracker.service;

import com.financeTracker.dto.NotificationDto;
import com.financeTracker.dto.TargetUserDto;
import com.financeTracker.model.type.NotificationType;

import java.util.List;

public interface NotificationService {

    void createNotification(NotificationType notificationType, TargetUserDto targetUserDto);

    List<NotificationDto> getAllByUserId(TargetUserDto targetUserDto);

    List<NotificationDto> getUnreadByUserId(TargetUserDto targetUserDto);

    void deleteAllByUserId(TargetUserDto targetUserDto);
}