package com.financeTracker.aspect;

import com.financeTracker.annotation.Auditable;
import com.financeTracker.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;


import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@Slf4j
@Aspect
public class AuditLoggingAspect {

    private static final Marker AUDIT_MARKER = MarkerFactory.getMarker("AUDIT");

    @Pointcut("@annotation(auditable)")
    private void annotatedByAuditable(Auditable auditable) {}

    @Before(value = "annotatedByAuditable(auditable)", argNames = "joinPoint,auditable")
    public void auditAndLogExecutionTime(JoinPoint joinPoint, Auditable auditable) {
        String logMessage = createMessage(joinPoint);
        log.info(AUDIT_MARKER, "[{}] {}", auditable.eventType().getDescription(), logMessage);
    }

    private String createMessage(JoinPoint joinPoint) {
        HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
        UserDto user = (UserDto) request.getSession().getAttribute("user");

        String userName = (user != null) ? user.getLogin() : "Guest";
        String remoteAddr = request.getRemoteAddr();

        return String.format("Пользователь: %s, IP: %s", userName, remoteAddr);
    }
}
