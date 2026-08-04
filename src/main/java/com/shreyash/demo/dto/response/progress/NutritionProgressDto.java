package com.shreyash.demo.dto.response.progress;

import java.util.List;

public class NutritionProgressDto {
	private Double averageCalories;

    private Double targetCalories;

    private Double averageProtein;

    private Double averageCarbs;

    private Double averageFat;

    private List<CaloriesDataPointDto> history;
}
