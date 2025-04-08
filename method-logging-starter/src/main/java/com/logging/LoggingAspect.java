package com.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("within(com.financeTracker.controller..*) " +
            "|| within(com.financeTracker.dao.impl..*) " +
            "|| within(com.financeTracker.service.impl..*)")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();

        if (!declaringTypeName.startsWith("com.financeTracker")) {
            return joinPoint.proceed();
        }

        log.info("[{}]: Calling method {}", getFormattedCurrentDate(), joinPoint.getSignature());

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis() - start;

        log.info("[{}]: Execution of method {} finished. Execution time is {} ms.",
                getFormattedCurrentDate(), joinPoint.getSignature(), end);
        return result;
    }

    private String getFormattedCurrentDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(dateTimeFormatter);
    }
}