package com.shreyash.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shreyash.demo.model.EmailVerificationToken;
import com.shreyash.demo.model.User;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    
    Optional<EmailVerificationToken> findTopByUserOrderByCreatedAtDesc(User user);

	
}
