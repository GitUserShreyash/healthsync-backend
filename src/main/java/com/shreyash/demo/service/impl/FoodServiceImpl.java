package com.shreyash.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shreyash.demo.dto.FoodResponse;
import com.shreyash.demo.mapper.FoodMapper;
import com.shreyash.demo.repo.FoodRepository;
import com.shreyash.demo.service.IFoodService;

@Service
public class FoodServiceImpl implements IFoodService {
	
	@Autowired
	private FoodRepository foodRepo;

	@Autowired
	private FoodMapper mapper;
	
	@Override
	public List<FoodResponse> getAllFoods() {
		return foodRepo.findAll().stream().map(mapper::toDto).toList();
	}

	@Override
	public List<FoodResponse> searchFoods(String query) {
		return foodRepo.findByFoodNameContainingIgnoreCase(query).stream().map(mapper::toDto).toList();
	}

	@Override
	public List<FoodResponse> getFoodsByCategory(String category) {
		return foodRepo.findByCategoryContainingIgnoreCase(category).stream().map(mapper::toDto).toList();
	}

}
