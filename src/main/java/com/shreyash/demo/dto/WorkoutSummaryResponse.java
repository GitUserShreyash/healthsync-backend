package com.shreyash.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSummaryResponse {

    private Integer workoutsThisWeek;

    private Integer totalMinutes;

    private Double totalCalories;

    private Integer currentStreak;
}