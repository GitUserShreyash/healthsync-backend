package com.shreyash.demo.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shreyash.demo.model.HydrationLog;
import com.shreyash.demo.model.User;

public interface HydrationLogRepository extends JpaRepository<HydrationLog, Long> {
    
    List<HydrationLog> findByUserOrderByLoggedAtDesc(User user);
    void deleteByIdAndUser(Long id, User user);
    
    
    List<HydrationLog> findByUserAndLoggedAtBetweenOrderByLoggedAtDesc(
            User user,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
