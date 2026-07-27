package com.shreyash.demo.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.shreyash.demo.dto.WorkoutPlanResponse;
import com.shreyash.demo.enums.WorkoutType;
import com.shreyash.demo.mapper.WorkoutPlanMapper;
import com.shreyash.demo.model.User;
import com.shreyash.demo.model.UserProfile;
import com.shreyash.demo.model.WorkoutLog;
import com.shreyash.demo.model.WorkoutPlan;
import com.shreyash.demo.repo.UserProfileRepository;
import com.shreyash.demo.repo.UserRepository;
import com.shreyash.demo.repo.WorkoutLogRepository;
import com.shreyash.demo.repo.WorkoutPlanRepository;

import jakarta.transaction.Transactional;

@Service
public class WorkoutPlanServiceImpl implements IWorkoutPlanService {

	@Autowired
	private WorkoutPlanRepository planRepo;
	
	@Autowired
	private WorkoutLogRepository workoutLogRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserProfileRepository profileRepo;
	
	@Autowired
	private WorkoutPlanMapper mapper;
	
	@Autowired
	private ICalculationService calculationService;
	
	private WorkoutPlan createPlan(User user, DayOfWeek day, WorkoutType type, Integer duration, String description) {

	    WorkoutPlan plan = new WorkoutPlan();

	    plan.setDay(day);
	    plan.setWorkoutType(type);
	    plan.setTargetDurationMinutes(duration);
	    plan.setDescription(description);

	    return plan;
	}
	
//	@Override
//	@Transactional
//	public void generatePlan(User user, UserProfile profile) {
//
//	    List<WorkoutPlan> plans = new ArrayList<>();
//
//	    plans.add(createPlan(user, DayOfWeek.MONDAY, WorkoutType.CHEST, 45,"Chest and Triceps"));
//
//	    plans.add(createPlan(user, DayOfWeek.TUESDAY, WorkoutType.BACK, 45, "Back and Biceps"));
//
//	    plans.add(createPlan(user, DayOfWeek.WEDNESDAY, WorkoutType.CARDIO, 30, "Cardio"));
//
//	    plans.add(createPlan(user, DayOfWeek.THURSDAY, WorkoutType.LEGS, 50, "Leg Day"));
//
//	    plans.add(createPlan(user, DayOfWeek.FRIDAY, WorkoutType.SHOULDERS, 40, "Shoulders"));
//
//	    plans.add(createPlan(user, DayOfWeek.SATURDAY, WorkoutType.FULL_BODY, 60, "Full Body"));
//
//	    plans.add(createPlan(user, DayOfWeek.SUNDAY, WorkoutType.REST, 0, "Recovery Day"));
//
//	    planRepo.saveAll(plans);
//	}

	@Override
	public List<WorkoutPlanResponse> getWeeklyPlan() {

	    String username = SecurityContextHolder
	            .getContext()
	            .getAuthentication()
	            .getName();

	    User user = userRepo.findByUsername(username)
	            .orElseThrow(() ->
	                    new RuntimeException("User not found"));
	    
	    return planRepo.findByGoalType(profileRepo.findByUser(user).get().getGoal())
	            .stream()
	            .map(mapper::toDto)
	            .toList();
	}

//	@Override
//	@Transactional
//	public String completeWorkout(Long planId) {
//
//	    String username =
//	            SecurityContextHolder.getContext()
//	                    .getAuthentication()
//	                    .getName();
//
//	    User user = userRepo.findByUsername(username)
//	            .orElseThrow(() ->
//	                    new RuntimeException(
//	                            "User not found"));
//
//	    UserProfile profile =
//	            profileRepo.findByUser(user)
//	                    .orElseThrow(() ->
//	                            new RuntimeException(
//	                                    "Profile not found"));
//
//	    WorkoutPlan plan =
//	            planRepo.findByIdAndUser(
//	                    planId,
//	                    user)
//	                    .orElseThrow(() ->
//	                            new RuntimeException(
//	                                    "Workout plan not found"));
//	    if (plan.getCompleted()) {
//	        throw new RuntimeException(
//	                "Workout already completed");
//	    }
//	    
//	    plan.setCompleted(true);
//	    planRepo.save(plan);
//	    
//	    WorkoutLog log =
//	            new WorkoutLog();
//
//	    log.setUser(user);
//	    log.setWorkoutType(
//	            plan.getWorkoutType());
//
//	    log.setDay(LocalDate.now());
//
//	    log.setDurationMinutes(
//	            plan.getTargetDurationMinutes());
//
//	    log.setCompleted(true);
//
//	    log.setLoggedAt(
//	            LocalDateTime.now());
//	    
//	    int calories =
//	            calculationService
//	                    .calculateCaloriesBurned(
//	                            plan.getWorkoutType(),
//	                            plan.getTargetDurationMinutes(),
//	                            profile.getWeightKg());
//
//	    log.setCaloriesBurned(
//	            calories);
//	    workoutLogRepo.save(log);
//
//	    return "Workout completed successfully";
//	    }

	@Override
	public WorkoutPlanResponse getTodayWorkout() {
		String username = SecurityContextHolder
	            .getContext()
	            .getAuthentication()
	            .getName();

	    User user = userRepo.findByUsername(username)
	            .orElseThrow(() ->
	                    new RuntimeException("User not found"));
	    
	    DayOfWeek today = LocalDate.now().getDayOfWeek();

	    WorkoutPlan workoutPlan =
	            planRepo.findByDayAndGoalType(today,profileRepo.findByUser(user).get().getGoal())
	                    .orElseThrow(() ->
	                            new RuntimeException("No workout available today"));

	    return mapper.toDto(workoutPlan);
	}


}
