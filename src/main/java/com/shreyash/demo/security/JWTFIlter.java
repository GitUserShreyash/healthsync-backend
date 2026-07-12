package com.shreyash.demo.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFIlter extends OncePerRequestFilter {

    @Autowired
    private JWTUtilizer jwtutilizer;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (uri.contains("/login")
                || uri.contains("/signup")
                || uri.contains("/verify-email")
                || uri.contains("/forgot-password")
                || uri.contains("/reset-password")
                || uri.contains("/validate-token")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization token required");
            return;
        }

        String token = authHeader.substring(7);
        Map<String, String> result = jwtutilizer.validateToken(token);

        String code = result.get("code");

        if (!"200".equals(code)) {
            if ("401".equals(code)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else if ("403".equals(code)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            response.getWriter().write(result.getOrDefault("error", "Invalid token"));
            return;
        }

        String role = result.get("role");
        String username = result.get("username");

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (uri.startsWith("/healthsync/admin") && !"ADMIN".equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access denied");
            return;
        }

        if (uri.startsWith("/healthsync/user") && !"User".equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access denied");
            return;
        }

        filterChain.doFilter(request, response);
    }
}