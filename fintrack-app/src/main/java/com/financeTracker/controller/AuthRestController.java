package com.financeTracker.controller;

import com.financeTracker.dto.AuthRequestDto;
import com.financeTracker.dto.UserDto;
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

    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя")
    public ResponseEntity<UserDto> register(@RequestBody AuthRequestDto authRequest) {
        UserDto registeredUser = userMapper.toUserDto(securityService.registration(authRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @PostMapping("/login")
    @Operation(summary = "Авторизация пользователя")
    public ResponseEntity<UserDto> login(@RequestBody AuthRequestDto authRequest, HttpSession session) {
        UserDto authorizedUser = userMapper.toUserDto(securityService.authorize(authRequest));
        session.setAttribute("user", authorizedUser);
        return ResponseEntity.ok(authorizedUser);
    }
}

