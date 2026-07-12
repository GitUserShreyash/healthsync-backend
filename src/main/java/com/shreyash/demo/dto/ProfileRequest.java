package com.shreyash.demo.dto;

import com.shreyash.demo.enums.ActivityLevel;
import com.shreyash.demo.enums.ExperienceLevel;
import com.shreyash.demo.enums.Gender;
import com.shreyash.demo.enums.GoalType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProfileRequest {

    @NotNull(message = "Age is required")
    @Min(value = 10, message = "Age must be at least 10")
    @Max(value = 120, message = "Age cannot exceed 120")
    private Integer age;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Height is required")
    @Min(value = 50, message = "Height must be at least 50 cm")
    @Max(value = 300, message = "Height cannot exceed 300 cm")
    private Double heightCm;

    @NotNull(message = "Weight is required")
    @Min(value = 20, message = "Weight must be at least 20 kg")
    @Max(value = 500, message = "Weight cannot exceed 500 kg")
    private Double weightKg;

    @NotNull(message = "Goal is required")
    private GoalType goal;

    @NotNull(message = "Activity level is required")
    private ActivityLevel activityLevel;

    @NotNull(message = "Experience level is required")
    private ExperienceLevel experienceLevel;

    private String timezone;
}