package com.financeTracker.mapper;

import com.financeTracker.dto.NotificationDto;
import com.financeTracker.model.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationDto toDto(Notification notification);
}
