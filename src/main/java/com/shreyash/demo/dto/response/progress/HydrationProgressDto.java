package com.shreyash.demo.dto.response.progress;

import java.util.List;

import lombok.Data;

@Data
public class HydrationProgressDto {
	 private Double averageWaterIntake;

	 private Double dailyGoal;

	 private Double completionPercentage;

	 private List<WaterDataPointDto> history;
}
