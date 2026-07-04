package com.shreyash.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shreyash.demo.dto.ChangePasswordRequest;
import com.shreyash.demo.dto.ForgotPasswordRequest;
import com.shreyash.demo.dto.LoginRequest;
import com.shreyash.demo.dto.LoginResponse;
import com.shreyash.demo.dto.ResetPasswordRequest;
import com.shreyash.demo.dto.UserResponse;
import com.shreyash.demo.dto.VerifyEmailRequest;
import com.shreyash.demo.model.User;
import com.shreyash.demo.repo.UserRepository;
import com.shreyash.demo.security.JWTUtilizer;
import com.shreyash.demo.service.IAuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
    private PasswordEncoder passwordEncoder;

	@Autowired
	private IAuthService authService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private JWTUtilizer jwtUtilizer;

    AuthController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
	
	@PostMapping("/verify-email")
	public ResponseEntity<String> verifyEmail(@RequestBody VerifyEmailRequest request){
		return ResponseEntity.ok(authService.verifyEmail(request));
	}
	
	@PostMapping("/user/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request){
		User user = userRepo.findByEmail(request.getEmail())
	            .orElse(null);

	    if (user == null) {
	        return ResponseEntity.status(401)
	                .body("Invalid credentials");
	    }

	    if (!passwordEncoder.matches(
	            request.getPassword(),
	            user.getPassword())) {

	        return ResponseEntity.status(401)
	                .body("Invalid credentials");
	    }
	    
	    String token = jwtUtilizer.generateJWTToken(user.getUsername(), "USER");
	    
	    return ResponseEntity.ok(new LoginResponse(token, "USER"));
	}
	
	@GetMapping("/me")
	public ResponseEntity<UserResponse> getCurrentUser(){
		return ResponseEntity.ok(authService.getCurrentUser());
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest req){
		return ResponseEntity.ok(authService.forgotPassword(req));
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest req){
		return ResponseEntity.ok(authService.resetPassword(req));
	}
	
	@PostMapping("/change-password")
	public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest req){
		return ResponseEntity.ok(authService.changePassword(req));
	}
}
