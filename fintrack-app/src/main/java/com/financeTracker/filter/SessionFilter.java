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

/**
 * Servlet filter that enforces session-based access control.
 * <p>
 * This filter runs once per request and ensures that:
 * <ul>
 *   <li>Unauthenticated users can only access public endpoints.</li>
 *   <li>Authenticated users cannot access login or registration pages.</li>
 *   <li>Blocked users are denied access to all endpoints.</li>
 * </ul>
 * <p>
 * Public endpoints are defined in the {@code PUBLIC_PATHS} list and may include Swagger UI routes,
 * which can be removed in production.
 */
@Component
public class SessionFilter extends OncePerRequestFilter {

    /**
     * List of endpoints that are publicly accessible (do not require authentication).
     */
    private static final List<String> PUBLIC_PATHS = List.of(
            "/login",
            "/register",
            // Swagger UI endpoints (can be removed in production)
            "/swagger-ui",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/v3/api-docs/",
            "/swagger-ui/index.html"
    );

    /**
     * Performs filtering logic on each request to determine access rights based on session presence.
     *
     * @param request the incoming HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain to proceed if the request is allowed
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);
        UserDto userDto = session != null ? (UserDto) session.getAttribute("user") : null;

        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(uri::contains);

        // Reject unauthenticated users trying to access non-public endpoints
        if (!isPublic && userDto == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            return;
        }

        // Reject authenticated users trying to access public endpoints like login/register
        if (isPublic && userDto != null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
            return;
        }

        // Reject users with status BLOCKED
        if (userDto != null && "BLOCKED".equals(userDto.getUserStatus())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
            return;
        }

        // Allow the request to proceed
        filterChain.doFilter(request, response);
    }
}

