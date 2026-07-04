package com.shreyash.demo.service;

import org.springframework.stereotype.Service;

import com.shreyash.demo.enums.ActivityLevel;
import com.shreyash.demo.enums.Gender;
import com.shreyash.demo.enums.WorkoutType;

@Service
public class CalculationServiceImpl implements ICalculationService {

	@Override
	public double calculateBmi(double weightKg, double heightCm) {
		return weightKg / Math.pow(heightCm / 100.0, 2);
	}

	@Override
	public String getBmiCategory(double bmi) {
		if (bmi < 18.5)
	        return "UNDERWEIGHT";
	    else if (bmi < 25)
	        return "NORMAL";
	    else if (bmi < 30)
	        return "OVERWEIGHT";
	    else
	        return "OBESE";
	}

	@Override
	public Double getBodyFat(Gender gender, double bmi, int age) {
		if (gender == Gender.MALE) {
		    return 1.20 * bmi +0.23 * age - 16.2;
		}
		else {
		    return 1.20 * bmi + 0.23 * age - 5.4;
		}
	}

	@Override
	public double calculateWaterIntake(double weightKg, ActivityLevel activityLevel) {
		double water = (weightKg * 35) / 1000;
		
		if (activityLevel == ActivityLevel.ACTIVE)
		    water += 0.5;
		else if (activityLevel == ActivityLevel.VERY_ACTIVE)
		    water += 1.0;
		
		return water;
	}

	@Override
	public int calculateCaloriesBurned(WorkoutType type, Integer duration, Double weightKg) {
		double met;

	    switch (type) {
	        case WALKING:
	            met = 3.5;
	            break;
	        case CARDIO:
	            met = 7;
	            break;
	        case CYCLING:
	            met = 8;
	            break;
	        case STRENGTH:
	            met = 6;
	            break;
	        case YOGA:
	            met = 3;
	            break;
	        default:
	            met = 5;
	    }

	    return (int)
	            ((met * 3.5 * weightKg / 200)
	                    * duration);
	}

}
