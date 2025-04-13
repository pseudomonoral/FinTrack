package com.audit.constants;

public interface AuditSql {
    String INSERT_AUDIT_SQL = """
                INSERT INTO audit_log (user_login, event_type, ip_address, created_at)
                VALUES (?, ?, ?, ?)
                """;

    String FIND_BY_LOGIN_AUDIT_SQL = """
                SELECT user_login, event_type, ip_address, created_at
                FROM audit_log
                WHERE user_login = ?
                ORDER BY created_at DESC
                """;

    String FIND_BY_IP_AUDIT_SQL = """
                SELECT user_login, event_type, ip_address, created_at
                FROM audit_log
                WHERE ip_address = ?
                ORDER BY created_at DESC
                """;

    String DELETE_ALL_AUDIT_SQL = """
                DELETE FROM audit_log
                """;
}
