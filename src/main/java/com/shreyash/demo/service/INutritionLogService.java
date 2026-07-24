package com.shreyash.demo.service;

import java.util.List;

import com.shreyash.demo.dto.DailyCaloriesResponse;
import com.shreyash.demo.dto.NutritionLogRequest;
import com.shreyash.demo.dto.NutritionLogResponse;

public interface INutritionLogService {

	NutritionLogResponse logNutrition(NutritionLogRequest req);
	String delete(long id);
	List<DailyCaloriesResponse> getDailyCaloriesHistory();
	List<NutritionLogResponse> getTodayNutritionLogs();
	Integer getTodayCalories();
}
