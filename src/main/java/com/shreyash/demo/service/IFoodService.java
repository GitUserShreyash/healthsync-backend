package com.shreyash.demo.service;

import java.util.List;

import com.shreyash.demo.dto.FoodResponse;

public interface IFoodService {
	List<FoodResponse> getAllFoods();

	List<FoodResponse> searchFoods(String query);

	List<FoodResponse> getFoodsByCategory(String category);
}
