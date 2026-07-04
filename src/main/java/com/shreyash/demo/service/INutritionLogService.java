package com.shreyash.demo.service;

import java.util.List;


import com.shreyash.demo.dto.NutritionLogRequest;
import com.shreyash.demo.dto.NutritionLogResponse;

public interface INutritionLogService {

	NutritionLogResponse logNutrition(NutritionLogRequest req);
	List<NutritionLogResponse> getHistory();
	String delete(long id);
}
