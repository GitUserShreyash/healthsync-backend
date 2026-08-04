package com.shreyash.demo.dto.response.progress;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutDataPointDto {
	private LocalDate date;

    private int durationMinutes;
    
    private double caloriesBurned;
}
