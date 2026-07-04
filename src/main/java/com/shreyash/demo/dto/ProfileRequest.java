package com.shreyash.demo.dto;

import com.shreyash.demo.enums.ActivityLevel;
import com.shreyash.demo.enums.ExperienceLevel;
import com.shreyash.demo.enums.Gender;
import com.shreyash.demo.enums.GoalType;

import lombok.Data;

@Data
public class ProfileRequest {
	private Integer age;
    private Gender gender;
    private Double heightCm;
    private Double weightKg;
    private GoalType goal;
    private ActivityLevel activityLevel;
    private ExperienceLevel experienceLevel;
    private String timezone;
}
