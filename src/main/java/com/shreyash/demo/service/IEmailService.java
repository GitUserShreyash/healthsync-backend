package com.shreyash.demo.service;

public interface IEmailService {
	public void sendEmail(String to, String subject, String body); 
	public void sendResetLink(String toEmail,String resetLink);
}
