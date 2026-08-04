package com.shreyash.demo.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.shreyash.demo.dto.BodyMetricResponse;
import com.shreyash.demo.mapper.BodyMetricsMapper;
import com.shreyash.demo.model.BodyMetrics;
import com.shreyash.demo.model.User;
import com.shreyash.demo.model.UserProfile;
import com.shreyash.demo.repo.BodyMetricsRepository;
import com.shreyash.demo.repo.UserProfileRepository;
import com.shreyash.demo.repo.UserRepository;
import com.shreyash.demo.service.IBodyMetricsService;

@Service
public class BodyMetricsServiceImpl implements IBodyMetricsService{

	@Autowired
	private BodyMetricsRepository metricsRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserProfileRepository userProfile;
	
	@Autowired
	private BodyMetricsMapper mapper;
	
	
	@Override
	public void logMetric(UserProfile profile, User user) {
		BodyMetrics metric;
		
		List<BodyMetrics> todayLogs = metricsRepo.findByUserAndRecordedAtBetweenOrderByRecordedAtDesc(user, LocalDate.now().atStartOfDay(), LocalDateTime.now());
		
				if(!todayLogs.isEmpty()) {
			        metric = todayLogs.get(0);
			    } else {
			        metric = new BodyMetrics();
			        metric.setUser(user);
			    }
		
		metric.setWeightKg(profile.getWeightKg());
		metric.setHeightCm(profile.getHeightCm());
		metric.setBmi(profile.getBmi());
		metric.setBmiCategory(profile.getBmiCategory());
		metric.setBodyFat(profile.getBodyFat());
		metric.setRecordedAt(LocalDateTime.now());

		metricsRepo.save(metric);
	}

	@Override
	public List<BodyMetricResponse> getHistory() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User Not Found"));
		
		return metricsRepo.findByUserOrderByRecordedAtDesc(user).stream().map(mapper::toDto).toList();
	}

}
