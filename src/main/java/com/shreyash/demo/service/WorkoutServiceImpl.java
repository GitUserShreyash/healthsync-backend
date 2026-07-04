package com.shreyash.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.shreyash.demo.dto.WorkoutLogRequest;
import com.shreyash.demo.dto.WorkoutLogResponse;
import com.shreyash.demo.mapper.WorkoutLogMapper;
import com.shreyash.demo.mapper.WorkoutPlanMapper;
import com.shreyash.demo.model.User;
import com.shreyash.demo.model.UserProfile;
import com.shreyash.demo.model.WorkoutLog;
import com.shreyash.demo.repo.UserProfileRepository;
import com.shreyash.demo.repo.UserRepository;
import com.shreyash.demo.repo.WorkoutLogRepository;

@Service
public class WorkoutServiceImpl implements IWorkoutService {

	@Autowired
	private WorkoutLogRepository workoutLogRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private WorkoutLogMapper mapper;
	
	@Autowired
	private ICalculationService calculationService;
	
	@Override
	public WorkoutLogResponse logWorkout(WorkoutLogRequest req) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
		
		UserProfile profile = userProfileRepo.findByUser(user).orElseThrow(()-> new RuntimeException("User profile not found"));
		
		WorkoutLog log = mapper.toEntity(req);
		log.setUser(user);
		log.setCompleted(true);
		log.setLoggedAt(LocalDateTime.now());
		int calories = calculationService.calculateCaloriesBurned(req.getWorkoutType(), req.getDurationMinutes(), profile.getWeightKg());
		log.setCaloriesBurned(calories);
		
		return mapper.toDto(workoutLogRepo.save(log));
	}

	@Override
	public List<WorkoutLogResponse> getHistory() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
		
		return workoutLogRepo.findByUserOrderByLoggedAtDesc(user).stream().map(mapper::toDto).toList();
	}

	@Override
	public String delete(Long id) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
		
		WorkoutLog log = workoutLogRepo.findByIdAndUser(id, user).orElseThrow(()-> new RuntimeException("Workout log not found"));
		
		workoutLogRepo.delete(log);
		return "Workout deleted successfully";
	}

}
