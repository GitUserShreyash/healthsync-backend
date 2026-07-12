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
import com.shreyash.demo.dto.SignupRequest;
import com.shreyash.demo.dto.UserResponse;
import com.shreyash.demo.dto.VerifyEmailRequest;
import com.shreyash.demo.model.User;
import com.shreyash.demo.repo.UserRepository;
import com.shreyash.demo.security.JWTUtilizer;
import com.shreyash.demo.service.IAuthService;

import jakarta.validation.Valid;

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
	
    @PostMapping("/user/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest req){
    	System.out.println("signup api is called");
    	return ResponseEntity.ok(authService.signup(req));
    }
    
	@PostMapping("/verify-email")
	public ResponseEntity<String> verifyEmail(@Valid @RequestBody VerifyEmailRequest request){
		System.out.println("verify-email api is called");
		return ResponseEntity.ok(authService.verifyEmail(request));
	}
	
	@PostMapping("/user/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
		System.out.println("login api is called");
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
		System.out.println("me api is called");
		return ResponseEntity.ok(authService.getCurrentUser());
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest req){
		System.out.println("forgot-password api is called");
		return ResponseEntity.ok(authService.forgotPassword(req));
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest req){
		return ResponseEntity.ok(authService.resetPassword(req));
	}
	
	@PostMapping("/change-password")
	public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest req){
		return ResponseEntity.ok(authService.changePassword(req));
	}
}
