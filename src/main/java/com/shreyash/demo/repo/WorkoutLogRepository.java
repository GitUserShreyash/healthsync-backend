package com.shreyash.demo.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shreyash.demo.model.User;
import com.shreyash.demo.model.WorkoutLog;

public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {
    
    List<WorkoutLog> findByUserOrderByLoggedAtDesc(User user);
    
    Optional<WorkoutLog> findByIdAndUser(Long id,User user);
    
    List<WorkoutLog> findTop30ByUserOrderByLoggedAtDesc(User user);
    
    long countByUserAndLoggedAtBetween(
            User user,
            LocalDateTime start,
            LocalDateTime end);
    
    @Query("""
    	       SELECT COALESCE(SUM(w.durationMinutes),0)
    	       FROM WorkoutLog w
    	       WHERE w.user = :user
    	       AND w.loggedAt BETWEEN :start AND :end
    	       """)
    	Integer getTotalMinutes(
    	        User user,
    	        LocalDateTime start,
    	        LocalDateTime end);
    
    @Query("""
    	       SELECT COALESCE(SUM(w.caloriesBurned),0)
    	       FROM WorkoutLog w
    	       WHERE w.user = :user
    	       AND w.loggedAt BETWEEN :start AND :end
    	       """)
    	Double getTotalCalories(
    	        User user,
    	        LocalDateTime start,
    	        LocalDateTime end);
    
    List<WorkoutLog> findByUserAndLoggedAtBetweenOrderByLoggedAtAsc(
            User user,
            LocalDateTime start,
            LocalDateTime end
    );
}
