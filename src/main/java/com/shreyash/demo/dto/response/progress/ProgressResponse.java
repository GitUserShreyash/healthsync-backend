package com.shreyash.demo.dto.response.progress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressResponse {
	private WeightProgressDto weight;

    private BmiProgressDto bmi;

    private HydrationProgressDto hydration;

    private WorkoutProgressDto workout;

    private NutritionProgressDto nutrition;

    //private GoalProgressDto goal;

}
