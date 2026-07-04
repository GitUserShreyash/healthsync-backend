package com.shreyash.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shreyash.demo.dto.FoodResponse;
import com.shreyash.demo.service.FoodServiceImpl;


@RestController
@RequestMapping("/api/foods")
public class FoodController {
	
	@Autowired
	public FoodServiceImpl foodService;
	
	@GetMapping
	public ResponseEntity<List<FoodResponse>> getAllFoods(){
		return ResponseEntity.ok(foodService.getAllFoods());
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<FoodResponse>> searchFood(@RequestParam String query){
		return ResponseEntity.ok(foodService.searchFoods(query));
	}
	
	@GetMapping("/category/(category}")
	public ResponseEntity<List<FoodResponse>> getByCategory(@PathVariable String category){
		return ResponseEntity.ok(foodService.getFoodsByCategory(category));
	}
}
