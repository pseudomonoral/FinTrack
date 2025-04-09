package com.financeTracker.controller;

import com.audit.Auditable;
import com.audit.EventType;
import com.financeTracker.dto.auth.AuthRequestDto;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.mapper.UserMapper;
import com.financeTracker.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "Регистрация и вход пользователей")
public class AuthRestController {

    private final SecurityService securityService;
    private final UserMapper userMapper;

    @Auditable(eventType = EventType.REGISTER)
    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя")
    public ResponseEntity<UserDto> register(@RequestBody AuthRequestDto authRequest) {
        UserDto registeredUser = userMapper.toUserDto(securityService.registration(authRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @Auditable(eventType = EventType.LOGIN)
    @PostMapping("/login")
    @Operation(summary = "Авторизация пользователя")
    public ResponseEntity<UserDto> login(@RequestBody AuthRequestDto authRequest, HttpSession session) {
        UserDto authorizedUser = userMapper.toUserDto(securityService.authorize(authRequest));
        session.setAttribute("user", authorizedUser);
        return ResponseEntity.ok(authorizedUser);
    }
}

