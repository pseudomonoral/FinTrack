package com.financeTracker.mapper;

import com.financeTracker.dto.notification.NotificationResponseDto;
import com.financeTracker.model.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponseDto toDto(Notification notification);
}
