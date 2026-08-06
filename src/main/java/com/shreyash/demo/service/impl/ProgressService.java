package com.shreyash.demo.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
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
		
		List<WaterDataPointDto> history = new ArrayList<>();
		
		for(LocalDate date = startDate; !date.isAfter(endDate); date=date.plusDays(1)) {
			history.add(
	                new WaterDataPointDto(
	                        date,
	                        dailyWater.getOrDefault(date, 0.0)
	                )
	        );
		}
		
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
		
		Map<LocalDate, Double> weightMap = metrics.stream()
	            .collect(Collectors.toMap(
	                    metric -> metric.getRecordedAt().toLocalDate(),
	                    BodyMetrics::getWeightKg,
	                    (oldValue, newValue) -> newValue,
	                    TreeMap::new
	            ));


	    List<WeightDataPointDto> history = new ArrayList<>();

	    Double lastWeight = null;


	    for(LocalDate date = startDate;
	        !date.isAfter(endDate);
	        date = date.plusDays(1)) {


	        if(weightMap.containsKey(date)) {
	            lastWeight = weightMap.get(date);
	        }


	        if(lastWeight != null) {
	            history.add(
	                new WeightDataPointDto(
	                    date,
	                    lastWeight
	                )
	            );
	        }
	    }


	    double currentWeight = history.isEmpty()
	            ? 0
	            : history.get(history.size()-1).getWeight();


	    double startingWeight = history.isEmpty()
	            ? 0
	            : history.get(0).getWeight();


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
	    dto.setStartingWeight(startingWeight);
	    dto.setHighestWeight(highestWeight);
	    dto.setLowestWeight(lowestWeight);
	    dto.setWeightChange(currentWeight - startingWeight);
	    dto.setHistory(history);


	    return dto;
	}
	
	//Workout Progress Retrieval
	private WorkoutProgressDto getWorkoutProgress(User user, LocalDate startDate, LocalDate endDate) {
		List<WorkoutLog> logs = workoutRepo.findByUserAndLoggedAtBetweenOrderByLoggedAtAsc(user, startDate.atStartOfDay(), endDate.atTime(23,59,59));
		
		Map<LocalDate, WorkoutDataPointDto> dailyWorkout = logs.stream()
	            .collect(Collectors.toMap(
	                    workout -> workout.getLoggedAt().toLocalDate(),
	                    workout -> new WorkoutDataPointDto(
	                            workout.getLoggedAt().toLocalDate(),
	                            workout.getDurationMinutes(),
	                            workout.getCaloriesBurned()
	                    ),
	                    (oldValue, newValue) -> newValue,
	                    TreeMap::new
	            ));


	    List<WorkoutDataPointDto> history = new ArrayList<>();

	    for (LocalDate date = startDate;
	         !date.isAfter(endDate);
	         date = date.plusDays(1)) {


	        if (dailyWorkout.containsKey(date)) {

	            history.add(dailyWorkout.get(date));

	        } else {

	            history.add(
	                    new WorkoutDataPointDto(
	                            date,
	                            0,
	                            0.0
	                    )
	            );
	        }
	    }
		
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
	                    Collectors.summingDouble(NutritionLog::getCalories)
	            ));


	    Map<LocalDate, Double> dailyProtein = logs.stream()
	            .collect(Collectors.groupingBy(
	                    log -> log.getLoggedAt().toLocalDate(),
	                    TreeMap::new,
	                    Collectors.summingDouble(NutritionLog::getProtein)
	            ));


	    Map<LocalDate, Double> dailyCarbs = logs.stream()
	            .collect(Collectors.groupingBy(
	                    log -> log.getLoggedAt().toLocalDate(),
	                    TreeMap::new,
	                    Collectors.summingDouble(NutritionLog::getCarbs)
	            ));


	    Map<LocalDate, Double> dailyFat = logs.stream()
	            .collect(Collectors.groupingBy(
	                    log -> log.getLoggedAt().toLocalDate(),
	                    TreeMap::new,
	                    Collectors.summingDouble(NutritionLog::getFat)
	            ));


	    List<CaloriesDataPointDto> history = new ArrayList<>();

	    double totalCalories = 0;
	    double totalProtein = 0;
	    double totalCarbs = 0;
	    double totalFat = 0;

	    int totalDays = 0;


	    for (LocalDate date = startDate;
	         !date.isAfter(endDate);
	         date = date.plusDays(1)) {


	        double calories = dailyCalories.getOrDefault(date, 0.0);
	        double protein = dailyProtein.getOrDefault(date, 0.0);
	        double carbs = dailyCarbs.getOrDefault(date, 0.0);
	        double fat = dailyFat.getOrDefault(date, 0.0);


	        history.add(
	                new CaloriesDataPointDto(
	                        date,
	                        calories
	                )
	        );


	        totalCalories += calories;
	        totalProtein += protein;
	        totalCarbs += carbs;
	        totalFat += fat;

	        totalDays++;
	    }


	    double avgCalories = totalDays == 0 ? 0 : totalCalories / totalDays;
	    double avgProtein = totalDays == 0 ? 0 : totalProtein / totalDays;
	    double avgCarbs = totalDays == 0 ? 0 : totalCarbs / totalDays;
	    double avgFat = totalDays == 0 ? 0 : totalFat / totalDays;


	    UserProfile profile = userProfileRepository
	            .findByUser(user)
	            .orElseThrow();


	    double targetCal = profile.getRecommendedCaloryIntake();


	    NutritionProgressDto dto = new NutritionProgressDto();

	    dto.setAverageCalories(avgCalories);
	    dto.setAverageProtein(avgProtein);
	    dto.setAverageCarbs(avgCarbs);
	    dto.setAverageFat(avgFat);
	    dto.setTargetCalories(targetCal);
	    dto.setHistory(history);

	    return dto;
	}
	
	// BMI Progress Retrieval
	private BmiProgressDto getBmiProgress(User user, LocalDate startDate, LocalDate endDate) {
		List<BodyMetrics> metrics = metricRepo.findByUserAndRecordedAtBetweenOrderByRecordedAtDesc(user, startDate.atStartOfDay(), endDate.atTime(23,59,59));
		
		Map<LocalDate, Double> bmiMap = metrics.stream()
	            .collect(Collectors.toMap(
	                    metric -> metric.getRecordedAt().toLocalDate(),
	                    BodyMetrics::getBmi,
	                    (oldValue, newValue) -> newValue,
	                    TreeMap::new
	            ));


	    List<BmiDataPointDto> history = new ArrayList<>();

	    Double lastBmi = null;


	    for (LocalDate date = startDate;
	         !date.isAfter(endDate);
	         date = date.plusDays(1)) {


	        if (bmiMap.containsKey(date)) {
	            lastBmi = bmiMap.get(date);
	        }


	        if (lastBmi != null) {

	            history.add(
	                    new BmiDataPointDto(
	                            date,
	                            lastBmi
	                    )
	            );
	        }
	    }


	    UserProfile profile = userProfileRepository
	            .findByUser(user)
	            .orElseThrow();


	    double currBmi = profile.getBmi();

	    String bmiCategory = profile.getBmiCategory();


	    BmiProgressDto dto = new BmiProgressDto(
	            currBmi,
	            bmiCategory,
	            history
	    );


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
