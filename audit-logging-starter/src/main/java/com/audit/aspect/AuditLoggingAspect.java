package com.audit.aspect;

import com.audit.model.AuditLog;
import com.audit.model.UserDto;
import com.audit.annotation.Auditable;
import com.audit.dao.AuditLogDao;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

/**
 * Aspect that handles audit logging for servlet methods annotated with {@link Auditable}.
 * <p>
 * Before executing a method annotated with {@code @Auditable}, this aspect logs
 * the event type, user login (from session), and IP address of the requester.
 * It also persists an audit log entry into the database using {@link AuditLogDao}.
 */
@Aspect
@RequiredArgsConstructor
@Slf4j
public class AuditLoggingAspect {

    private static final Marker AUDIT_MARKER = MarkerFactory.getMarker("AUDIT");

    private final AuditLogDao auditLogDao;

    /**
     * Pointcut that matches any method annotated with {@link Auditable}.
     *
     * @param auditable the {@link Auditable} annotation instance
     */
    @Pointcut("@annotation(auditable)")
    private void annotatedByAuditable(Auditable auditable) {
        // Pointcut definition, no implementation needed
    }

    /**
     * Before advice that intercepts methods annotated with {@link Auditable}
     * and logs audit information.
     *
     * <p>Captured data includes:</p>
     * <ul>
     *     <li>User login from session attribute {@code "user"}</li>
     *     <li>Client IP address</li>
     *     <li>Audit event type from the {@code @Auditable} annotation</li>
     *     <li>Current timestamp</li>
     * </ul>
     *
     * <p>This information is both logged and persisted via {@link AuditLogDao}.</p>
     *
     * @param auditable the {@link Auditable} annotation on the intercepted method
     */
    @Before(value = "annotatedByAuditable(auditable)", argNames = "auditable")
    public void audit(Auditable auditable) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        UserDto user = (UserDto) request.getSession().getAttribute("user");

        String login = (user != null) ? user.getLogin() : "Guest";
        String ip = request.getRemoteAddr();
        String event = auditable.eventType().getDescription();

        log.info(AUDIT_MARKER, "[{}] Пользователь: {}, IP: {}", event, login, ip);

        auditLogDao.save(new AuditLog(login, event, ip, LocalDateTime.now()));
    }
}

