package com.shreyash.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.shreyash.demo.dto.NutritionLogRequest;
import com.shreyash.demo.dto.NutritionLogResponse;
import com.shreyash.demo.mapper.NutritionLogMapper;
import com.shreyash.demo.model.NutritionLog;
import com.shreyash.demo.model.User;
import com.shreyash.demo.repo.NutritionLogRepository;
import com.shreyash.demo.repo.UserRepository;

public class NutritionLogServiceImpl implements INutritionLogService {
	
	@Autowired
	private NutritionLogRepository nutritionLogRepo;

	@Autowired
	private NutritionLogMapper nutritionLogMapper;
	
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public NutritionLogResponse logNutrition(NutritionLogRequest req) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
		
		NutritionLog log = nutritionLogMapper.toEntity(req);
		log.setUser(user);
		log.setLoggedAt(LocalDateTime.now());
		
		return nutritionLogMapper.toDto(nutritionLogRepo.save(log));
	}

	@Override
	public List<NutritionLogResponse> getHistory() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
		
		return nutritionLogRepo.findByUserOrderByLoggedAtDesc(user).stream().map(nutritionLogMapper::toDto).toList();
	}

	@Override
	public String delete(long id) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
		
		NutritionLog log = nutritionLogRepo.findById(id).orElseThrow(()->new RuntimeException("Nutrition Not Found"));
		
		if (!log.getUser().getId().equals(user.getId())) {
	        throw new RuntimeException(
	                "You cannot delete another user's log");
	    }
		
		nutritionLogRepo.deleteById(id);
		return "Nutrition Log Deleted Successfully";
	}

}
