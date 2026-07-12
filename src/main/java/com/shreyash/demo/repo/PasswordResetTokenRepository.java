package com.shreyash.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shreyash.demo.model.PasswordResetToken;
import com.shreyash.demo.model.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
    
    void deleteByUser(User user);

    Optional<PasswordResetToken> findTopByUserOrderByCreatedAtDesc(User user);
}
