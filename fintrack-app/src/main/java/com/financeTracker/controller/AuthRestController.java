package com.financeTracker.controller;

import com.audit.annotation.Auditable;
import com.audit.model.EventType;
import com.financeTracker.dto.auth.AuthRequestDto;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.mapper.UserMapper;
import com.financeTracker.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "Регистрация и вход пользователей")
@Validated
public class AuthRestController {

    private final SecurityService securityService;
    private final UserMapper userMapper;

    @Auditable(eventType = EventType.REGISTER)
    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody AuthRequestDto authRequest) {
        UserDto registeredUser = userMapper.toUserDto(securityService.registration(authRequest));
        URI location = URI.create("/users/" + registeredUser.getId());
        return ResponseEntity.created(location).body(registeredUser);
    }

    @Auditable(eventType = EventType.LOGIN)
    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@Valid @RequestBody AuthRequestDto authRequest,
                                         @Parameter(hidden = true) HttpSession session) {
        UserDto authorizedUser = userMapper.toUserDto(securityService.authorize(authRequest));
        session.setAttribute("user", authorizedUser);
        session.setAttribute("userLogin", authorizedUser.getLogin());
        return ResponseEntity.ok(authorizedUser);
    }
}


