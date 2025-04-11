package com.audit.dao;

import com.audit.model.AuditLog;

import java.util.List;

public interface AuditLogDao {
    void save(AuditLog entry);
    List<AuditLog> findByLogin(String login);
    List<AuditLog> findByIp(String ip);
    void deleteAll();
}