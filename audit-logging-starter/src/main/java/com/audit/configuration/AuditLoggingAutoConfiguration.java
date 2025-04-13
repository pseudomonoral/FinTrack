package com.audit.configuration;

import com.audit.aspect.AuditLoggingAspect;
import com.audit.controller.AuditLogController;
import com.audit.dao.AuditLogDao;
import com.audit.dao.impl.AuditLogDaoImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnClass(AuditLoggingAspect.class)
@RequiredArgsConstructor
public class AuditLoggingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AuditLogDao auditLogDao(DataSource dataSource) {
        return new AuditLogDaoImpl(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuditLoggingAspect auditLoggingAspect(AuditLogDao auditLogDao) {
        return new AuditLoggingAspect(auditLogDao);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuditLogController auditLogController(AuditLogDao auditLogDao) {
        return new AuditLogController(auditLogDao);
    }
}
