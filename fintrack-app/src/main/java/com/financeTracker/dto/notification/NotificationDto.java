package com.financeTracker.dto.notification;

import com.financeTracker.model.type.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private LocalDateTime createdAt;
    private Long userId;
    private NotificationType type;
}
