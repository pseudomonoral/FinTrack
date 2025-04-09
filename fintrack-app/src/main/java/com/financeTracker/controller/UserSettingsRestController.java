package com.financeTracker.controller;

import com.audit.Auditable;
import com.audit.EventType;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.dto.user.UserUpdateDto;
import com.financeTracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
@Tag(name = "Настройки пользователя", description = "Просмотр, обновление и удаление профиля")
@Validated
public class UserSettingsRestController {

    private final UserService userService;

    @Operation(summary = "Получение текущего пользователя")
    @GetMapping
    public ResponseEntity<UserDto> getCurrentUser(
            @Parameter(hidden = true) HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Обновление профиля пользователя")
    @Auditable(eventType = EventType.UPDATE_PROFILE)
    @PutMapping
    public ResponseEntity<Void> updateUserProfile(
            @Valid @RequestBody UserUpdateDto userUpdateDto,
            @Parameter(hidden = true) HttpSession session) {

        UserDto user = (UserDto) session.getAttribute("user");

        userService.updateUser(user.getId(), userUpdateDto);
        UserDto updatedUser = userService.getUserById(user.getId());
        session.setAttribute("user", updatedUser);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Удаление аккаунта пользователя")
    @Auditable(eventType = EventType.DELETE_ACCOUNT)
    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(
            @Parameter(hidden = true) HttpSession session) {

        UserDto user = (UserDto) session.getAttribute("user");

        userService.deleteUserById(user.getId());
        session.invalidate();

        return ResponseEntity.noContent().build();
    }
}


