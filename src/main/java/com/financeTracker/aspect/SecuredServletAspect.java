package com.financeTracker.aspect;


import com.financeTracker.annotation.Secured;
import com.financeTracker.dto.UserDto;
import com.financeTracker.model.type.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


@Aspect
public class SecuredServletAspect {
    @Pointcut("@annotation(secured) && execution(* com.financeTracker.servlet.*Servlet.*(..))")
    public void callAtSecuredMethod(Secured secured) {
    }

    @Around(value = "callAtSecuredMethod(secured) && args(.., req, resp)", argNames = "joinPoint,secured,req,resp")
    public Object checkRole(ProceedingJoinPoint joinPoint, Secured secured, HttpServletRequest req, HttpServletResponse resp) throws Throwable {
        UserDto currentUser = (UserDto) req.getSession().getAttribute("user");
        if ((currentUser.getUserStatus() == null || !UserRole.valueOf(currentUser.getUserRole()).equals(UserRole.valueOf(secured.hasRole())))) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        return joinPoint.proceed();
    }
}