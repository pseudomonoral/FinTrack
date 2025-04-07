package com.financeTracker.servlet;

import com.financeTracker.dto.UserDto;
import com.financeTracker.model.type.UserStatus;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@WebFilter("/*")
public class ServletFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if (!requestURI.contains("login") && !requestURI.contains("register") && (session == null || session.getAttribute("user") == null)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if ((requestURI.contains("login") || requestURI.contains("register")) && session != null && session.getAttribute("user") != null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (session != null){
            UserDto userDto = (UserDto) session.getAttribute("user");
            if (userDto != null && Objects.equals(userDto.getUserStatus(), UserStatus.BLOCKED.toString())) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        chain.doFilter(req, resp);
    }
}