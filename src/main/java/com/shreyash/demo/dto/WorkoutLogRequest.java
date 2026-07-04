package com.shreyash.demo.dto;

import java.time.LocalDate;

import com.shreyash.demo.enums.WorkoutIntesity;
import com.shreyash.demo.enums.WorkoutType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkoutLogRequest {

    @NotNull(message = "Workout type is required")
    private WorkoutType workoutType;

    @NotNull(message = "Workout date is required")
    private LocalDate day;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be greater than 0")
    private Integer durationMinutes;

    @NotNull(message = "Intensity is required")
    private WorkoutIntesity intensity;

    private String notes;
}