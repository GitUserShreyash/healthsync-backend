package com.shreyash.demo.dto.response.progress;

import java.util.List;

import lombok.Data;

@Data
public class WorkoutProgressDto {
	private int completedWorkouts;

    private int totalMinutes;

    private double totalCalories;
    
    private List<WorkoutDataPointDto> history;
}
