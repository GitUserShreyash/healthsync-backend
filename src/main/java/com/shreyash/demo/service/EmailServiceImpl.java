package com.shreyash.demo.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.shreyash.demo.model.Email;
import com.shreyash.demo.repo.EmailRepository;

@Service
public class EmailServiceImpl implements IEmailService {
	
	@Autowired
	private EmailRepository emailRepo;
	
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void sendEmail(String to, String subject, String body) {
		Email email = new Email();
		email.setMessage(body);
		email.setSubject(subject);
		email.setRecipient(to);
		email.setSentAt(LocalDateTime.now());
		
		try {
			SimpleMailMessage sms = new SimpleMailMessage();
			sms.setSubject(subject);
			sms.setTo(to);
			sms.setText(body);
			
			mailSender.send(sms);
			email.setStatus("Success");
			System.out.println("email sent successfully...");
		} catch (Exception e) {
			email.setStatus("Failure");
			System.out.println("email could not be sent...");
			System.out.println(e.getMessage());
		}
		
		emailRepo.save(email);

	}

	@Override
	public void sendResetLink(String toEmail, String otp) {
		String subject = "HealthSync Password Reset OTP";
		String body = """
	            Hello,

	            We received a request to reset your password.

	            Your OTP is:

	            %s

	            This OTP is valid for 10 minutes.

	            If you didn't request this, please ignore this email.

	            Regards,
	            HealthSync Team
	            """.formatted(otp);
		
		sendEmail(toEmail, subject, body);
	}

}
