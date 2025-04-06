package com.financeTracker.controller;

import com.financeTracker.annotation.Auditable;
import com.financeTracker.dto.UserDto;
import com.financeTracker.dto.UserUpdateDto;
import com.financeTracker.model.type.EventType;
import com.financeTracker.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
public class UserSettingsRestController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDto> getCurrentUser(HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        return ResponseEntity.ok(user);
    }

    @PutMapping
    @Auditable(eventType = EventType.UPDATE_PROFILE)
    public ResponseEntity<Void> updateUserProfile(
            @Valid @RequestBody UserUpdateDto userUpdateDto,
            HttpSession session
    ) {
        UserDto user = (UserDto) session.getAttribute("user");

        userService.updateUser(user.getId(), userUpdateDto);
        UserDto updatedUser = userService.getUserById(user.getId());
        session.setAttribute("user", updatedUser);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Auditable(eventType = EventType.DELETE_ACCOUNT)
    public ResponseEntity<Void> deleteAccount(HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");

        userService.deleteUserById(user.getId());
        session.invalidate();

        return ResponseEntity.noContent().build();
    }
}

