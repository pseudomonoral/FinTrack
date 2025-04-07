package com.audit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
public class AuditLoggingAspect {

    private static final Marker AUDIT_MARKER = MarkerFactory.getMarker("AUDIT");

    @Pointcut("@annotation(auditable)")
    private void annotatedByAuditable(Auditable auditable) {}

    @Before(value = "annotatedByAuditable(auditable)", argNames = "joinPoint,auditable")
    public void audit(JoinPoint joinPoint, Auditable auditable) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        UserDto user = (UserDto) request.getSession().getAttribute("user");
        String userName = (user != null) ? user.getLogin() : "Guest";
        String remoteAddr = request.getRemoteAddr();

        log.info(AUDIT_MARKER, "[{}] Пользователь: {}, IP: {}", auditable.eventType().getDescription(),
                userName != null ? userName : "Guest", remoteAddr);
    }
}
