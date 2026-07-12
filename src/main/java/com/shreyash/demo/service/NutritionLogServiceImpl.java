package com.shreyash.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.shreyash.demo.dto.DailyCaloriesResponse;
import com.shreyash.demo.dto.NutritionLogRequest;
import com.shreyash.demo.dto.NutritionLogResponse;
import com.shreyash.demo.exception.ResourceNotFoundException;
import com.shreyash.demo.mapper.NutritionLogMapper;
import com.shreyash.demo.model.NutritionLog;
import com.shreyash.demo.model.User;
import com.shreyash.demo.repo.NutritionLogRepository;
import com.shreyash.demo.repo.UserRepository;

@Service
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

	@Override
	public List<DailyCaloriesResponse> getDailyCalories() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		User user = userRepo.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("User not found"));
		
		LocalDate startDate = LocalDate.now().minusDays(13);
		List<Object[]> result = nutritionLogRepo.getDailyCalories(user, startDate.atStartOfDay());
		Map<LocalDate, Double> calorieMap = new HashMap<>();
		
		for (Object[] row : result) {
			LocalDate date;
			if (row[0] instanceof LocalDate ld) {
	            date = ld;
	        } else {
	            date = ((java.sql.Date) row[0]).toLocalDate();
	        }
			Double calories = ((Number) row[1]).doubleValue();
			calorieMap.put(date, calories);
		}
		List<DailyCaloriesResponse> response = new ArrayList<>();

	    for (int i = 0; i < 14; i++) {

	        LocalDate date = startDate.plusDays(i);

	        response.add(new DailyCaloriesResponse(date,calorieMap.getOrDefault(date,0.0)));
	    }

	    return response;
	}



}
