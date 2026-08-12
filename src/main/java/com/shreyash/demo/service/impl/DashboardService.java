package com.shreyash.demo.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.shreyash.demo.dto.DashBoardResponse;
import com.shreyash.demo.model.User;
import com.shreyash.demo.model.UserProfile;
import com.shreyash.demo.repo.UserProfileRepository;
import com.shreyash.demo.repo.UserRepository;
import com.shreyash.demo.repo.WorkoutLogRepository;
import com.shreyash.demo.service.INutritionLogService;

@Service
public class DashboardService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserProfileRepository profileRepo;
	
	@Autowired
	private INutritionLogService nutritionService;
	
	@Autowired
	private WorkoutLogRepository workoutRepo;
	
	public DashBoardResponse getDashboard() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
		UserProfile profile = profileRepo.findByUser(user).orElseThrow(()-> new RuntimeException("User profile not found"));
		DashBoardResponse resp = new DashBoardResponse();
		resp.setBmi(profile.getBmi());
		resp.setBmiCategory(profile.getBmiCategory());
		resp.setUsername(username);
		resp.setCurrentWeightKg(profile.getWeightKg());
		resp.setHydrationStreakDays(profile.getHydrationStreakDays());
		resp.setDailyWaterGoalL(profile.getRecommendedWaterIntakeL());
		resp.setGoal(profile.getGoal());
		resp.setWorkoutsCompletedThisWeek(
					workoutRepo.countByUserAndLoggedAtBetween(
							user, 
							LocalDate
								.now()
								.minusDays(6)
								.atStartOfDay(), 
							LocalDate
								.now()
								.atTime(23, 59, 59)
					));
		resp.setCaloriesConsumedToday(nutritionService.getTodayCalories());
		System.out.println("DashboardService:"+profile.getAppName());
		resp.setAppName(profile.getAppName());
		System.out.println("DashboardService:"+resp);
		return resp;
	}
}
