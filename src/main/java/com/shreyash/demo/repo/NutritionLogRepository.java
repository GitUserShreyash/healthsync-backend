package com.shreyash.demo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shreyash.demo.model.NutritionLog;
import com.shreyash.demo.model.User;

public interface NutritionLogRepository extends JpaRepository<NutritionLog, Long> {
	Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    
    List<NutritionLog> findByUserOrderByLoggedAtDesc(User user);
}
