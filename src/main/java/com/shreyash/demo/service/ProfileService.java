package com.shreyash.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.shreyash.demo.dto.ProfileRequest;
import com.shreyash.demo.dto.ProfileResponse;
import com.shreyash.demo.mapper.ProfileMapper;
import com.shreyash.demo.model.User;
import com.shreyash.demo.model.UserProfile;
import com.shreyash.demo.repo.UserProfileRepository;
import com.shreyash.demo.repo.UserRepository;

@Service
public class ProfileService implements IProfileService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserProfileRepository userProfileRepo;

	@Autowired
	private ProfileMapper profileMapper;
	
	@Autowired
	private ICalculationService calculationService;
	
	@Autowired
	private IWorkoutPlanService workoutPlanService;
	
	@Autowired
	private IBodyMetricsService metricsService;
	
	private UserProfile setBodyMetrics(UserProfile profile) {
		double bmi = calculationService.calculateBmi(profile.getWeightKg(), profile.getHeightCm());
		profile.setBmi(bmi);
		profile.setBmiCategory(calculationService.getBmiCategory(bmi));
		profile.setBodyFat(calculationService.getBodyFat(profile.getGender(), bmi, profile.getAge()));
		profile.setRecommendedWaterIntakeL(calculationService.calculateWaterIntake(profile.getWeightKg(), profile.getActivityLevel()));
		profile.setRecommendedCaloryIntake(calculationService.calculateDailyCalorieGoal(profile));
		profile.setProfileCompleted(true);
		
		return profile;
	}
	
	@Override
	public ProfileResponse getProfile() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User Not Found"));
		
		UserProfile userProfile = userProfileRepo.findByUser(user).orElseThrow(()-> new RuntimeException("User not found"));
		
		return profileMapper.toDto(userProfile);
	}

	@Override
	public ProfileResponse updateProfile(ProfileRequest req) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User Not Found"));
		
		UserProfile profile = userProfileRepo.findByUser(user).orElse(new UserProfile());
		
		if (profile.getId() == null) {
		    profile.setUser(user);
		}

		profileMapper.updateEntity(req, profile);

		profile = setBodyMetrics(profile);
		
		metricsService.logMetric(profile, user);
		
		return profileMapper.toDto(
		        userProfileRepo.save(profile));
	}

}
