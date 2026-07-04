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
public class JWTFIlter extends OncePerRequestFilter{

	@Autowired
	private JWTUtilizer jwtutilizer;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String uri = request.getRequestURI();

		if (uri.contains("/login")
                || uri.contains("/register")
                || uri.contains("/forgot-password")
                || uri.contains("/reset-password")
                || uri.contains("/validate-token")) {

            filterChain.doFilter(request, response);
            return;
        }
		
		String authHeader = request.getHeader("Authorization");
		
		if(authHeader!=null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			
			Map<String, String> result = jwtutilizer.validateToken(token);
			
			if(result.get("code").equals("200")) {
				
				String role = result.get("role");
				String username = result.get("username");
				
				UsernamePasswordAuthenticationToken authentication =
		                new UsernamePasswordAuthenticationToken(
		                        username,
		                        null,
		                        Collections.emptyList());

		        SecurityContextHolder.getContext()
		                             .setAuthentication(authentication);
		        
		        if(uri.startsWith("/healthsync/admin") && !role.equals("ADMIN")) {
					
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					
					response.getWriter().write("Access denied");
					
					return;
				}
				
				if(uri.startsWith("/healthsync/user") && !role.equals("User")) {
					
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					
					response.getWriter().write("Access denied");
					
					return;
				}else {
					response.setStatus(401);
					response.getWriter()
					        .write("Invalid token");
				}
			}
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.getWriter().write("Authorization token required");
		
	}
	}

}
