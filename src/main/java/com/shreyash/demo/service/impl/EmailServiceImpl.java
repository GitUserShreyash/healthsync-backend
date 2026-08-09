package com.shreyash.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.shreyash.demo.model.Email;
import com.shreyash.demo.repo.EmailRepository;
import com.shreyash.demo.service.IEmailService;

@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private EmailRepository emailRepo;

    @Autowired
    private RestClient restClient;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Override
    public void sendEmail(String to, String subject, String body) {

        Email email = new Email();
        email.setMessage(body);
        email.setSubject(subject);
        email.setRecipient(to);
        email.setSentAt(LocalDateTime.now());

        try {

            Map<String, Object> requestBody = Map.of(
                "sender", Map.of(
                    "name", "HealthSync",
                    "email", senderEmail
                ),
                "to", List.of(
                    Map.of("email", to)
                ),
                "subject", subject,
                "textContent", body
            );

            restClient.post()
                    .uri("/v3/smtp/email")
                    .header("api-key", brevoApiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .toBodilessEntity();

            email.setStatus("Success");

            System.out.println("========== EMAIL SENT THROUGH BREVO ==========");

        } catch (Exception e) {

            email.setStatus("Failure");

            System.out.println("========== BREVO EMAIL FAILED ==========");
            e.printStackTrace();
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