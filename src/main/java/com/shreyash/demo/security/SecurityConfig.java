package com.shreyash.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JWTFIlter jwtFilter;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.csrf(csrf -> csrf.disable()
				.authorizeHttpRequests(auth -> 
					 auth.requestMatchers(
							 "/api/auth/signup",
						        "/api/auth/login",
						        "/api/auth/verify-email",
						        "/api/auth/resend-otp",
						        "/api/auth/forgot-password",
						        "/api/auth/reset-password")
					 .permitAll()
					 .anyRequest().authenticated()
				)
		 
				.sessionManagement(session->
				 	session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				)
		
				.addFilterBefore(jwtFilter,  UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
}
