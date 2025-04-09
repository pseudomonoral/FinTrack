package com.financeTracker.controller;

import com.financeTracker.dto.notification.NotificationDto;
import com.financeTracker.dto.user.TargetUserDto;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@Tag(name = "Уведомления", description = "Получение и удаление уведомлений пользователя")
@Validated
public class NotificationRestController {

    private final NotificationService notificationService;

    @Operation(summary = "Получить все уведомления пользователя")
    @GetMapping("/all")
    public ResponseEntity<List<NotificationDto>> getAllNotifications(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user) {
        TargetUserDto targetUserDto = new TargetUserDto(user.getId());
        List<NotificationDto> notifications = notificationService.getAllByUserId(targetUserDto);
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "Получить только непрочитанные уведомления")
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDto>> getUnreadNotifications(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user) {
        TargetUserDto targetUserDto = new TargetUserDto(user.getId());
        List<NotificationDto> unreadNotifications = notificationService.getUnreadByUserId(targetUserDto);
        return ResponseEntity.ok(unreadNotifications);
    }

    @Operation(summary = "Удалить все уведомления пользователя")
    @DeleteMapping
    public ResponseEntity<Void> deleteAllNotifications(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user) {
        TargetUserDto targetUserDto = new TargetUserDto(user.getId());
        notificationService.deleteAllByUserId(targetUserDto);
        return ResponseEntity.noContent().build();
    }
}