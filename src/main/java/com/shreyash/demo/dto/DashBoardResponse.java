package com.shreyash.demo.dto;

import com.shreyash.demo.enums.GoalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashBoardResponse {

    private String username;

    private Double currentWeightKg;
    private Double bmi;
    private String bmiCategory;

    private Double dailyWaterGoalL;
    private Integer hydrationStreakDays;

    private Integer caloriesConsumedToday;
    private Integer workoutsCompletedThisWeek;

    private GoalType goal;
    private String recommendation;
}
