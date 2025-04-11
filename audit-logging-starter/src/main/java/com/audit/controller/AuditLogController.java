package com.audit.controller;

import com.audit.model.AuditLog;
import com.audit.dao.AuditLogDao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogDao auditLogDao;

    @GetMapping("/by-login")
    public List<AuditLog> getAuditLogByLogin(@RequestParam String login) {
        return auditLogDao.findByLogin(login);
    }

    @GetMapping("/by-ip")
    public List<AuditLog> getAuditLogByIp(@RequestParam String ip) {
        return auditLogDao.findByIp(ip);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAuditLog(){
        auditLogDao.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
