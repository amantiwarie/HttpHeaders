package org.app.springbootdemo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This filter runs BEFORE any controller for every incoming request
 * (that matches /api/employees/**, configured below).
 *
 * It checks for a header: X-Api-Key
 * If missing or incorrect -> request is rejected with 401, controller is never called.
 * If correct -> request passes through to the controller normally.
 */
@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String HEADER_NAME = "X-Api-Key";

    // Value is read from application.properties: app.api.key=...
    @Value("${app.api.key}")
    private String validApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();

        // Only protect employee endpoints; let everything else (e.g. /health) pass through
        if (requestPath.startsWith("/api/employees")) {

            String apiKeyHeader = request.getHeader(HEADER_NAME);

            if (apiKeyHeader == null || !apiKeyHeader.equals(validApiKey)) {
                // Reject immediately - controller code is never reached
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Unauthorized - missing or invalid X-Api-Key header\"}");
                return; // IMPORTANT: stop the chain here
            }
        }

        // Key valid (or path not protected) -> continue to next filter / controller
        filterChain.doFilter(request, response);
    }
}
