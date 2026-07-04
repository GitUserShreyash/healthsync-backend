package com.shreyash.demo.service;

import com.shreyash.demo.dto.ChangePasswordRequest;
import com.shreyash.demo.dto.ForgotPasswordRequest;
import com.shreyash.demo.dto.ResetPasswordRequest;
import com.shreyash.demo.dto.SignupRequest;
import com.shreyash.demo.dto.UserResponse;
import com.shreyash.demo.dto.VerifyEmailRequest;

public interface IAuthService {
	public String signup(SignupRequest req);
	public String verifyEmail(VerifyEmailRequest req);
	public UserResponse getCurrentUser();
	public String forgotPassword(ForgotPasswordRequest req);
	public String resetPassword(ResetPasswordRequest req);
	public String changePassword(ChangePasswordRequest req);
}
