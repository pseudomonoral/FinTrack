package com.financeTracker.controller;

import com.financeTracker.dto.NotificationDto;
import com.financeTracker.dto.TargetUserDto;
import com.financeTracker.dto.UserDto;
import com.financeTracker.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationRestController {

    private final NotificationService notificationService;

    @GetMapping("/all")
    public ResponseEntity<List<NotificationDto>> getAllNotifications(@SessionAttribute("user") UserDto user) {
        TargetUserDto targetUserDto = new TargetUserDto(user.getId());
        List<NotificationDto> notifications = notificationService.getAllByUserId(targetUserDto);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDto>> getUnreadNotifications(@SessionAttribute("user") UserDto user) {
        TargetUserDto targetUserDto = new TargetUserDto(user.getId());
        List<NotificationDto> unreadNotifications = notificationService.getUnreadByUserId(targetUserDto);
        return ResponseEntity.ok(unreadNotifications);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllNotifications(@SessionAttribute("user") UserDto user) {
        TargetUserDto targetUserDto = new TargetUserDto(user.getId());
        notificationService.deleteAllByUserId(targetUserDto);
        return ResponseEntity.noContent().build();
    }
}