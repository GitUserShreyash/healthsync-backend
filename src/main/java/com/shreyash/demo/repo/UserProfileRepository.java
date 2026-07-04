package com.shreyash.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shreyash.demo.model.User;
import com.shreyash.demo.model.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
	Optional<UserProfile> findByUsername(String username);

    Optional<UserProfile> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    
    Optional<UserProfile> findByUser(User user);
}
