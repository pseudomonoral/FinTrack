package com.financeTracker.aspect;

import com.financeTracker.annotation.Secured;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.model.type.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


/**
 * Aspect that enforces security for servlet methods annotated with {@link Secured}.
 * <p>
 * Before executing a method annotated with {@code @Secured}, this aspect checks whether
 * the current user has the required role specified in the annotation. If not, the
 * HTTP response is set to 403 Forbidden, and the method execution is blocked.
 */
@Aspect
@Component
public class SecuredServletAspect {

    /**
     * Pointcut that matches any method annotated with {@link Secured}.
     *
     * @param secured the {@link Secured} annotation instance
     */
    @Pointcut("@annotation(secured)")
    public void callAtSecuredMethod(Secured secured) {
        // Pointcut definition, no implementation needed
    }

    /**
     * Around advice that intercepts methods annotated with {@link Secured}
     * and checks whether the current user's role matches the required role.
     *
     * @param joinPoint the intercepted method
     * @param secured the {@link Secured} annotation containing the required role
     * @param req the current HTTP request
     * @param resp the current HTTP response
     * @return the result of the method execution, or {@code null} if access is denied
     * @throws Throwable if the intercepted method throws any exception
     */
    @Around(value = "callAtSecuredMethod(secured) && args(.., req, resp)", argNames = "joinPoint,secured,req,resp")
    public Object checkRole(ProceedingJoinPoint joinPoint, Secured secured,
                            HttpServletRequest req, HttpServletResponse resp) throws Throwable {
        UserDto currentUser = (UserDto) req.getSession().getAttribute("user");

        // Check if the user is authenticated and has the required role
        if (currentUser.getUserStatus() == null ||
                !UserRole.valueOf(currentUser.getUserRole()).equals(UserRole.valueOf(secured.hasRole()))) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
            return null;
        }

        // Proceed with the original method execution
        return joinPoint.proceed();
    }
}