package com.shreyash.demo.service.impl;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.shreyash.demo.dto.response.progress.HydrationProgressDto;
import com.shreyash.demo.dto.response.progress.WaterDataPointDto;
import com.shreyash.demo.dto.response.progress.WeightDataPointDto;
import com.shreyash.demo.dto.response.progress.WeightProgressDto;
import com.shreyash.demo.dto.response.progress.WorkoutDataPointDto;
import com.shreyash.demo.dto.response.progress.WorkoutProgressDto;
import com.shreyash.demo.model.BodyMetrics;
import com.shreyash.demo.model.HydrationLog;
import com.shreyash.demo.model.User;
import com.shreyash.demo.model.UserProfile;
import com.shreyash.demo.model.WorkoutLog;
import com.shreyash.demo.repo.BodyMetricsRepository;
import com.shreyash.demo.repo.HydrationLogRepository;
import com.shreyash.demo.repo.NutritionLogRepository;
import com.shreyash.demo.repo.UserProfileRepository;
import com.shreyash.demo.repo.WorkoutLogRepository;

@Service
public class ProgressService {
	private final BodyMetricsRepository metricRepo;
	private final HydrationLogRepository hydrationRepo;
	private final NutritionLogRepository nutritionRepo;
	private final WorkoutLogRepository workoutRepo;
	private final UserProfileRepository userProfileRepository;
	
	public ProgressService(BodyMetricsRepository metricRepo, HydrationLogRepository hydrationRepo,
								NutritionLogRepository nutritionRepo, WorkoutLogRepository workoutRepo,
								UserProfileRepository userProfileRepository) {
		this.hydrationRepo=hydrationRepo;
		this.metricRepo=metricRepo;
		this.nutritionRepo=nutritionRepo;
		this.workoutRepo=workoutRepo;
		this.userProfileRepository=userProfileRepository;
	}
	
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
	
}
