package com.financeTracker.filter;

import com.financeTracker.dto.user.UserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class SessionFilter extends OncePerRequestFilter {

    private static final List<String> PUBLIC_PATHS = List.of(
            "/login",
            "/register",
            // Swagger UI endpoints (can be removed or disabled in production)
            "/swagger-ui",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/v3/api-docs/",
            "/swagger-ui/index.html"
    );


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);
        UserDto userDto = session != null ? (UserDto) session.getAttribute("user") : null;

        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(uri::contains);

        if (!isPublic && userDto == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (isPublic && userDto != null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (userDto != null && "BLOCKED".equals(userDto.getUserStatus())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(request, response);
    }
}

