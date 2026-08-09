package com.shreyash.demo.service.impl;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shreyash.demo.dto.ChangePasswordRequest;
import com.shreyash.demo.dto.ForgotPasswordRequest;
import com.shreyash.demo.dto.ResetPasswordRequest;
import com.shreyash.demo.dto.SignupRequest;
import com.shreyash.demo.dto.UserResponse;
import com.shreyash.demo.dto.VerifyEmailRequest;
import com.shreyash.demo.mapper.DTOMapper;
import com.shreyash.demo.model.EmailVerificationToken;
import com.shreyash.demo.model.PasswordResetToken;
import com.shreyash.demo.model.User;
import com.shreyash.demo.model.UserProfile;
import com.shreyash.demo.repo.EmailVerificationTokenRepository;
import com.shreyash.demo.repo.PasswordResetTokenRepository;
import com.shreyash.demo.repo.UserProfileRepository;
import com.shreyash.demo.repo.UserRepository;
import com.shreyash.demo.service.IAuthService;


@Service
public class AuthServiceImpl implements IAuthService{
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private EmailServiceImpl emailService;
	
	@Autowired
	private EmailVerificationTokenRepository emailTokenRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private DTOMapper dtoMapper;
	
	@Autowired
	private PasswordResetTokenRepository passwordResetRepo;
	
	@Autowired
	private UserProfileRepository profileRepo;
	
	private String generateOTP() {
		return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
	}
	
	public String signup(SignupRequest req) {
		if(userRepo.existsByUsername(req.getUsername())) {
			throw new RuntimeException("Username already exists");
		}
		
		if(userRepo.existsByEmail(req.getEmail())) {
			throw new RuntimeException("email already exists");
		}
		
		
		User user = new User();
		user.setEmail(req.getEmail());
		user.setUsername(req.getUsername());
		user.setPassword(passwordEncoder.encode(req.getPassword()));
		
		User savedUser = userRepo.save(user);
		UserProfile profile = new UserProfile();
		
		profile.setUser(savedUser);
		profileRepo.save(profile);
		String genOTP = generateOTP();
		
		EmailVerificationToken evt = new EmailVerificationToken();
		evt.setOtp(genOTP);
		evt.setExpiresAt(LocalDateTime.now().plusMinutes(10));
		evt.setUser(savedUser);
		evt.setVerified(false);
		emailTokenRepo.save(evt);
		
		String subject = "Verify Your Email Address - HealthSync OTP";
		
		String body = """
		        Hello,

		        Thank you for registering with HealthSync.

		        To complete your email verification, please use the following One-Time Password (OTP):

		        OTP: %s

		        This OTP is valid for 10 minutes. Please do not share this code with anyone for security reasons.

		        If you did not create a HealthSync account, please ignore this email.

		        Regards,
		        HealthSync Team
		        Your Personal Fitness & Nutrition Companion
		        """.formatted(genOTP);
		
		emailService.sendEmail(savedUser.getEmail(), subject, body);
		
		return "OTP sent Successful";
	}
	
	public String verifyEmail(VerifyEmailRequest req) {
		User user = userRepo.findByEmail(req.getEmail()).orElseThrow(()-> new RuntimeException("User not found"));
		
		EmailVerificationToken token = emailTokenRepo.findTopByUserOrderByCreatedAtDesc(user).orElseThrow(()-> new RuntimeException("OTP not found"));
		
		if (Boolean.TRUE.equals(token.getVerified())) {
	        throw new RuntimeException("Email already verified");
	    }
		
		if (!token.getOtp().equals(req.getOtp())) {
	        throw new RuntimeException("Invalid OTP");
	    }
		
		if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
	        throw new RuntimeException("OTP expired");
	    }
		
		token.setVerified(true);

	    emailTokenRepo.save(token);

	    return "Email verified successfully";
	}
	
	public UserResponse getCurrentUser() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User Not Found"));
		
		return dtoMapper.mapToDTO(user);
	}
	
	public String forgotPassword(ForgotPasswordRequest req) {
		User user = userRepo.findByEmail(req.getEmail()).orElseThrow(()-> new RuntimeException("UserNotFound"));
		
		passwordResetRepo.deleteByUser(user);
		
		String OTP = generateOTP();
		
		PasswordResetToken token = new PasswordResetToken();
		token.setOtp(OTP);
		token.setExpiresAt(LocalDateTime.now().plusMinutes(10));
		token.setUser(user);
		emailService.sendResetLink(user.getEmail(), OTP);
		
		return "Password reset OTP sent.";
	}
	
	public String resetPassword(ResetPasswordRequest req) {
		User user = userRepo.findByEmail(req.getEmail()).orElseThrow(()->new RuntimeException("User not found"));
		
		PasswordResetToken token = passwordResetRepo.findTopByUserOrderByCreatedAtDesc(user).orElseThrow(()->new RuntimeException("OTP not found"));
		
		if(!req.getNewPassword().equals(req.getConfirmPassword())) {
			throw new RuntimeException("Passwords do not match");
		}
		
		user.setPassword(passwordEncoder.encode(req.getNewPassword()));
		
		userRepo.save(user);
		
		passwordResetRepo.delete(token);
		return "Password reset successfully";
	}
	
	public String changePassword(ChangePasswordRequest req) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
		
		if(!passwordEncoder.matches(req.getCurrentPassword(),user.getPassword())) {
			throw new RuntimeException("Current password is incorrect");
		}
		
		if(!req.getNewPassword().equals(req.getConfirmPassword())) {
			throw new RuntimeException("Passwords do not match");
		}
		
		user.setPassword(passwordEncoder.encode(req.getNewPassword()));
		
		return "Password changed Successfully";
	}
}
