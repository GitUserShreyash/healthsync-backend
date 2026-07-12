package com.shreyash.demo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shreyash.demo.model.BodyMetrics;
import com.shreyash.demo.model.User;

public interface BodyMetricsRepository extends JpaRepository<BodyMetrics, Long> {
	
    
    List<BodyMetrics> findByUserOrderByRecordedAtDesc(User user);
}
