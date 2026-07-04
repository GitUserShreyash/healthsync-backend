package com.shreyash.demo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shreyash.demo.model.HydrationLog;
import com.shreyash.demo.model.User;

public interface HydrationLogRepository extends JpaRepository<HydrationLog, Long> {
	Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    
    List<HydrationLog> findByUserOrderByLoggedAtDesc(User user);
}
