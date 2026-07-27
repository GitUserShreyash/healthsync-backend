package com.shreyash.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.shreyash.demo.enums.WorkoutIntensity;
import com.shreyash.demo.enums.WorkoutType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutLogResponse {

    private Long id;

    private WorkoutType workoutType;

    private LocalDate day;

    private Integer durationMinutes;

    private WorkoutIntensity intensity;

    private Integer caloriesBurned;

    private Boolean completed;

    private String notes;

    private LocalDateTime loggedAt;
}