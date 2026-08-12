package com.shreyash.demo.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shreyash.demo.dto.WorkoutLogRequest;
import com.shreyash.demo.dto.WorkoutLogResponse;
import com.shreyash.demo.dto.WorkoutSummaryResponse;
import com.shreyash.demo.exception.DuplicateResourceException;
import com.shreyash.demo.exception.ResourceNotFoundException;
import com.shreyash.demo.mapper.WorkoutLogMapper;
import com.shreyash.demo.model.User;
import com.shreyash.demo.model.UserProfile;
import com.shreyash.demo.model.WorkoutLog;
import com.shreyash.demo.repo.UserProfileRepository;
import com.shreyash.demo.repo.UserRepository;
import com.shreyash.demo.repo.WorkoutLogRepository;
import com.shreyash.demo.service.ICalculationService;
import com.shreyash.demo.service.IWorkoutService;

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
	
	private int calculateCurrentStreak(User user) {

	    List<WorkoutLog> logs =
	            workoutLogRepo.findTop30ByUserOrderByLoggedAtDesc(user);

	    if (logs.isEmpty()) {
	        return 0;
	    }

	    Set<LocalDate> workoutDays = logs.stream()
	    								.map(log -> log.getLoggedAt().toLocalDate())
	    								.collect(Collectors.toSet());

	    int streak = 0;

	    LocalDate current = LocalDate.now();

	    while (workoutDays.contains(current)) {
	        streak++;
	        current = current.minusDays(1);
	    }

	    return streak;
	}
	
	@Override
	public WorkoutLogResponse logWorkout(WorkoutLogRequest req) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
		
		UserProfile profile = userProfileRepo.findByUser(user).orElseThrow(()-> new RuntimeException("User profile not found"));
		
		System.out.println(req.toString());
		List<WorkoutLog> todaysLog = workoutLogRepo.findByUserAndLoggedAtBetweenOrderByLoggedAtAsc(user, LocalDate.now().atStartOfDay(), LocalDate.now().atTime(23, 59, 59));
		if(todaysLog.size()==1) {
			throw new DuplicateResourceException("Workout is already logged");
		}
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
	@Transactional
	public String delete(Long id) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
		
		WorkoutLog log = workoutLogRepo.findByIdAndUser(id, user).orElseThrow(()-> new RuntimeException("Workout log not found"));
		
		workoutLogRepo.delete(log);
		return "Workout deleted successfully";
	}

	@Override
	public WorkoutSummaryResponse getWorkoutSummary() {

	    User user = userRepo.findByUsername(
	            SecurityContextHolder.getContext().getAuthentication().getName())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    LocalDate today = LocalDate.now();

	    LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);

	    LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

	    LocalDateTime start = startOfWeek.atStartOfDay();

	    LocalDateTime end = endOfWeek.atTime(LocalTime.MAX);

	    long workouts =
	            workoutLogRepo.countByUserAndLoggedAtBetween(
	                    user,
	                    start,
	                    end);

	    Integer totalMinutes =
	            workoutLogRepo.getTotalMinutes(
	                    user,
	                    start,
	                    end);

	    Double totalCalories =
	            workoutLogRepo.getTotalCalories(
	                    user,
	                    start,
	                    end);

	    int streak = calculateCurrentStreak(user);

	    return WorkoutSummaryResponse.builder()
	            .workoutsThisWeek((int) workouts)
	            .totalMinutes(totalMinutes == null ? 0 : totalMinutes)
	            .totalCalories(totalCalories == null ? 0 : totalCalories)
	            .currentStreak(streak)
	            .build();
	}

	@Override
	public Boolean getTodayStatus() {
		User user = userRepo.findByUsername(
	            SecurityContextHolder.getContext().getAuthentication().getName())
	            .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		
		List<WorkoutLog> log = workoutLogRepo.
								findByUserAndLoggedAtBetweenOrderByLoggedAtAsc(
									user, 
									LocalDate.now().atStartOfDay(),
									LocalDate.now().atTime(23, 59, 59)
								);
		
		if(log.isEmpty()) {
			return false;
		}
		return true;
	}

}
