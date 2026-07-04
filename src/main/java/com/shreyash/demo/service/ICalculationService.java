package com.shreyash.demo.service;

import com.shreyash.demo.enums.ActivityLevel;
import com.shreyash.demo.enums.Gender;
import com.shreyash.demo.enums.WorkoutType;

public interface ICalculationService {
	double calculateBmi(double weightKg, double heightCm);
	String getBmiCategory(double bmi);
	Double getBodyFat(Gender gender, double bmi, int age);
	double calculateWaterIntake(double weightKg, ActivityLevel activityLevel);
	int calculateCaloriesBurned(WorkoutType type, Integer duration, Double weightKg);
}
