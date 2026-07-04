package com.shreyash.demo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shreyash.demo.model.User;
import com.shreyash.demo.model.WorkoutLog;

public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {
	Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    
    List<WorkoutLog> findByUserOrderByLoggedAtDesc(User user);
    
    Optional<WorkoutLog> findByIdAndUser(Long id,User user);
}
