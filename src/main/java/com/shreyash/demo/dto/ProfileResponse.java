package com.shreyash.demo.dto;

import com.shreyash.demo.enums.ActivityLevel;
import com.shreyash.demo.enums.ExperienceLevel;
import com.shreyash.demo.enums.Gender;
import com.shreyash.demo.enums.GoalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
	private Long userId;
    private String username;
    private String email;
    private Integer age;
    private Gender gender;
    private Double heightCm;
    private Double weightKg;
    private GoalType goal;
    private ActivityLevel activityLevel;
    private ExperienceLevel experienceLevel;
    private String timezone;
    private Double bmi;
    private String bmiCategory;
    private Double recommendedWaterIntakeL;
    private Integer hydrationStreakDays;
    private Boolean profileCompleted;
}
