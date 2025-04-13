package com.financeTracker.controller;

import com.audit.annotation.Auditable;
import com.audit.model.EventType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.audit.model.EventType.*;

@RestController
@RequestMapping("/logout")
@Tag(name = "Выход", description = "Завершение пользовательской сессии")
public class LogoutRestController {

    @Auditable(eventType = LOGOUT)
    @Operation(summary = "Выход пользователя из системы")
    @GetMapping
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}

