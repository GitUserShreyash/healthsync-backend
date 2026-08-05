package com.shreyash.demo.service.impl;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.shreyash.demo.dto.response.progress.BmiDataPointDto;
import com.shreyash.demo.dto.response.progress.BmiProgressDto;
import com.shreyash.demo.dto.response.progress.CaloriesDataPointDto;
import com.shreyash.demo.dto.response.progress.HydrationProgressDto;
import com.shreyash.demo.dto.response.progress.NutritionProgressDto;
import com.shreyash.demo.dto.response.progress.ProgressResponse;
import com.shreyash.demo.dto.response.progress.WaterDataPointDto;
import com.shreyash.demo.dto.response.progress.WeightDataPointDto;
import com.shreyash.demo.dto.response.progress.WeightProgressDto;
import com.shreyash.demo.dto.response.progress.WorkoutDataPointDto;
import com.shreyash.demo.dto.response.progress.WorkoutProgressDto;
import com.shreyash.demo.model.BodyMetrics;
import com.shreyash.demo.model.HydrationLog;
import com.shreyash.demo.model.NutritionLog;
import com.shreyash.demo.model.User;
import com.shreyash.demo.model.UserProfile;
import com.shreyash.demo.model.WorkoutLog;
import com.shreyash.demo.repo.BodyMetricsRepository;
import com.shreyash.demo.repo.HydrationLogRepository;
import com.shreyash.demo.repo.NutritionLogRepository;
import com.shreyash.demo.repo.UserProfileRepository;
import com.shreyash.demo.repo.UserRepository;
import com.shreyash.demo.repo.WorkoutLogRepository;

@Service
public class ProgressService {
	private final BodyMetricsRepository metricRepo;
	private final HydrationLogRepository hydrationRepo;
	private final NutritionLogRepository nutritionRepo;
	private final WorkoutLogRepository workoutRepo;
	private final UserProfileRepository userProfileRepository;
	private final UserRepository userRepo;
	
	public ProgressService(BodyMetricsRepository metricRepo, HydrationLogRepository hydrationRepo,
								NutritionLogRepository nutritionRepo, WorkoutLogRepository workoutRepo,
								UserProfileRepository userProfileRepository,
								UserRepository userRepo) {
		this.hydrationRepo=hydrationRepo;
		this.metricRepo=metricRepo;
		this.nutritionRepo=nutritionRepo;
		this.workoutRepo=workoutRepo;
		this.userProfileRepository=userProfileRepository;
		this.userRepo=userRepo;
	}
	
	// Hydration Progress Retrieval
	private HydrationProgressDto getHydrationProgress(User user, LocalDate startDate, LocalDate endDate) {
		List<HydrationLog> logs = hydrationRepo.findByUserAndLoggedAtBetweenOrderByLoggedAtDesc(user, startDate.atStartOfDay(), endDate.atTime(23,59,59));
		
		Map<LocalDate, Double> dailyWater = logs.stream()
												.collect(Collectors.groupingBy(
														log -> log.getLoggedAt().toLocalDate(),
														TreeMap::new,
														Collectors.summingDouble(
									                            log -> log.getAmountMl() / 1000.0
									                    )
													));
		
		List<WaterDataPointDto> history = dailyWater.entrySet()
													.stream()
													.map(entry ->
								                    		new WaterDataPointDto(
									                            entry.getKey(),
									                            entry.getValue()
									                    	)
													)
													.toList();
		double averageWater = history.stream()
								.mapToDouble(WaterDataPointDto::getLiters)
								.average()
								.orElse(0);
		
		UserProfile profile =
	            userProfileRepository.findByUser(user)
	            .orElseThrow();
		
		double dailyGoal = profile.getRecommendedWaterIntakeL();
		
		double completion = dailyGoal==0?0:(averageWater/dailyGoal)*100;
		
		HydrationProgressDto dto = new HydrationProgressDto();
		dto.setAverageWaterIntake(averageWater);
		dto.setCompletionPercentage(completion);
		dto.setDailyGoal(dailyGoal);
		dto.setHistory(history);
		return dto;
	}
	
	// Weight Progress Retrieval
	private WeightProgressDto getWeightProgress(User user, LocalDate startDate, LocalDate endDate) {
		List<BodyMetrics> metrics = metricRepo.findByUserAndRecordedAtBetweenOrderByRecordedAtDesc(user, startDate.atStartOfDay(), endDate.atTime(23,59,59));
		
		List<WeightDataPointDto> history =
	            metrics.stream()
	            .map(metric ->
	                    new WeightDataPointDto(
	                            metric.getRecordedAt().toLocalDate(),
	                            metric.getWeightKg()
	                    )
	            )
	            .sorted(Comparator.comparing(WeightDataPointDto::getDate))
	            .toList();
		
		double currentWeight = history.isEmpty()?0:history.get(history.size()-1).getWeight();
		
		double startingWeight = history.isEmpty()?0:history.get(0).getWeight();
		
		double highestWeight = history.stream()
									.mapToDouble(WeightDataPointDto::getWeight)
									.max()
									.orElse(0);
		
		double lowestWeight = history.stream()
									.mapToDouble(WeightDataPointDto::getWeight)
									.min()
									.orElse(0);
		
		WeightProgressDto dto = new WeightProgressDto();
		dto.setCurrentWeight(currentWeight);
		dto.setHighestWeight(highestWeight);
		dto.setHistory(history);
		dto.setLowestWeight(lowestWeight);
		dto.setStartingWeight(startingWeight);
		dto.setWeightChange(currentWeight-startingWeight);
		
		return dto;
	}
	
	//Workout Progress Retrieval
	private WorkoutProgressDto getWorkoutProgress(User user, LocalDate startDate, LocalDate endDate) {
		List<WorkoutLog> logs = workoutRepo.findByUserAndLoggedAtBetweenOrderByLoggedAtAsc(user, startDate.atStartOfDay(), endDate.atTime(23,59,59));
		
		List<WorkoutDataPointDto> history = logs.stream()
												.map(workout ->
														new WorkoutDataPointDto(
																workout.getLoggedAt().toLocalDate(),
																workout.getDurationMinutes(),
																workout.getCaloriesBurned()
														)
												)
												.toList();
		
		Integer totalMinutes = workoutRepo.getTotalMinutes(user, startDate.atStartOfDay(), endDate.atTime(23,59,59));
		
		Double totalCalories = workoutRepo.getTotalCalories(user, startDate.atStartOfDay(), endDate.atTime(23,59,59));
		
		WorkoutProgressDto dto = new WorkoutProgressDto();
		dto.setCompletedWorkouts(logs.size());
		dto.setHistory(history);
		dto.setTotalMinutes(totalMinutes);
		dto.setTotalCalories(totalCalories);
		
		return dto;
	}
	
	// Nutrition Progress Retrieval
	private NutritionProgressDto getNutritionProgress(User user, LocalDate startDate, LocalDate endDate) {
		List<NutritionLog> logs = nutritionRepo.findByUserAndLoggedAtBetweenOrderByLoggedAtAsc(user, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
		
		Map<LocalDate, Double> dailyCalories = logs.stream()
									.collect(Collectors.groupingBy(
										log -> log.getLoggedAt().toLocalDate(),
										TreeMap::new,
										Collectors.summingDouble(
									           log -> log.getCalories()
									    )
									));
		
		List<CaloriesDataPointDto> history = dailyCalories.entrySet()
										        .stream()
										        .map(entry -> new CaloriesDataPointDto(
										                entry.getKey(),
										                entry.getValue()
										        ))
										        .toList();
		
		Double avgCalories = history.stream()
								.mapToDouble(CaloriesDataPointDto :: getCalories)
								.average()
								.orElse(0);
		
		
		Map<LocalDate, Double> dailyProtein = logs.stream()
												.collect(Collectors.groupingBy(
														log -> log.getLoggedAt().toLocalDate(),
														TreeMap::new,
														Collectors.summingDouble(NutritionLog::getProtein)
												));
		
		Double avgProtein = dailyProtein.values()
								.stream()
								.mapToDouble(Double:: doubleValue)
								.average()
								.orElse(0);
		
		Map<LocalDate, Double> dailyCarbs = logs.stream()
												.collect(Collectors.groupingBy(
														log -> log.getLoggedAt().toLocalDate(),
														TreeMap::new,
														Collectors.summingDouble(NutritionLog::getCarbs)
												));
		
		Double avgCarbs = dailyCarbs.values()
								.stream()
								.mapToDouble(Double:: doubleValue)
								.average()
								.orElse(0);
		
		Map<LocalDate, Double> dailyFat = logs.stream()
												.collect(Collectors.groupingBy(
														log -> log.getLoggedAt().toLocalDate(),
														TreeMap::new,
														Collectors.summingDouble(NutritionLog::getFat)
												));

		Double avgFat = dailyFat.values()
								.stream()
								.mapToDouble(Double:: doubleValue)
								.average()
								.orElse(0);
		
		UserProfile profile =userProfileRepository.findByUser(user)
					            .orElseThrow();
		
		double targetCal = profile.getRecommendedCaloryIntake();
		
		NutritionProgressDto dto = new NutritionProgressDto();
		dto.setAverageCalories(avgCalories);
		dto.setAverageCarbs(avgCarbs);
		dto.setAverageFat(avgFat);
		dto.setAverageProtein(avgProtein);
		dto.setTargetCalories(targetCal);
		dto.setHistory(history);
		
		return dto;
	}
	
	// BMI Progress Retrieval
	private BmiProgressDto getBmiProgress(User user, LocalDate startDate, LocalDate endDate) {
		List<BodyMetrics> metrics = metricRepo.findByUserAndRecordedAtBetweenOrderByRecordedAtDesc(user, startDate.atStartOfDay(), endDate.atTime(23,59,59));
		
		List<BmiDataPointDto> history =	metrics.stream()
										            .map(metric ->
										                    new BmiDataPointDto(
										                            metric.getRecordedAt().toLocalDate(),
										                            metric.getBmi()
										                    )
										            )
										            .sorted(Comparator.comparing(BmiDataPointDto::getDate))
										            .toList();
		
		
		UserProfile profile =userProfileRepository.findByUser(user)
	            							.orElseThrow();
		
		double currBmi = profile.getBmi();
		String bmiCategory = profile.getBmiCategory();
		BmiProgressDto dto = new BmiProgressDto(currBmi, bmiCategory, history);
		
		return dto;
	}
	
	// Service Method Which uses helper method to seed ProgressResponse and return it
	public ProgressResponse getProgress(Integer days) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User Not Found"));
		LocalDate endDate = LocalDate.now();
	    LocalDate startDate = endDate.minusDays(days);
	    
		return new ProgressResponse(
				getWeightProgress(user, startDate, endDate),
				getBmiProgress(user, startDate, endDate),
				getHydrationProgress(user, startDate, endDate),
				getWorkoutProgress(user, startDate, endDate),
				getNutritionProgress(user, startDate, endDate)
				);	
	}
}
