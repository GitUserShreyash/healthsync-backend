package com.shreyash.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shreyash.demo.model.PasswordResetToken;
import com.shreyash.demo.model.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
	Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    
    void deleteByUser(User user);

    Optional<PasswordResetToken> findTopByUserOrderByCreatedAtDesc(User user);
}
