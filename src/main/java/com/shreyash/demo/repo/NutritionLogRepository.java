package com.shreyash.demo.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shreyash.demo.model.NutritionLog;
import com.shreyash.demo.model.User;

public interface NutritionLogRepository extends JpaRepository<NutritionLog, Long> {
    
    List<NutritionLog> findByUserAndLoggedAtGreaterThanEqualOrderByLoggedAtDesc(User user,LocalDateTime start);
    
    @Query("""
    		SELECT DATE(n.loggedAt), SUM(n.calories)
    		FROM NutritionLog n
    		WHERE n.user = :user
    		AND n.loggedAt >= :startDate
    		GROUP BY DATE(n.loggedAt)
    		ORDER BY DATE(n.loggedAt)
    		""")
    		List<Object[]> getDailyCalories(
    		        @Param("user") User user,
    		        @Param("startDate") LocalDateTime startDate
    		);
    		
    		@Query("""
    			    SELECT COALESCE(SUM(n.calories), 0)
    			    FROM NutritionLog n
    			    WHERE n.user = :user
    			      AND n.loggedAt >= :startOfDay
    			""")
    			Integer getTodayCalories(User user, LocalDateTime startOfDay);
}
