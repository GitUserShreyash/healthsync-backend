package com.shreyash.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shreyash.demo.model.EmailVerificationToken;
import com.shreyash.demo.model.User;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
	Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    
    Optional<EmailVerificationToken> findTopByUserOrderByCreatedAtDesc(User user);

	
}
