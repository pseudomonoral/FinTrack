package com.audit.controller;

import com.audit.model.AuditLog;
import com.audit.dao.AuditLogDao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
@Tag(name = "Аудит", description = "Просмотр и удаление логов аудита (доступно только администраторам)")
public class AuditLogController {

    private final AuditLogDao auditLogDao;

    @Operation(summary = "Получить логи по логину пользователя")
    @GetMapping("/by-login")
    public List<AuditLog> getAuditLogByLogin(
            @Parameter(description = "Логин пользователя", required = true)
            @RequestParam String login) {
        return auditLogDao.findByLogin(login);
    }

    @Operation(summary = "Получить логи по IP-адресу")
    @GetMapping("/by-ip")
    public List<AuditLog> getAuditLogByIp(
            @Parameter(description = "IP-адрес", required = true)
            @RequestParam String ip) {
        return auditLogDao.findByIp(ip);
    }

    @Operation(summary = "Удалить все записи логов")
    @DeleteMapping
    public ResponseEntity<Void> deleteAuditLog() {
        auditLogDao.deleteAll();
        return ResponseEntity.noContent().build();
    }
}

