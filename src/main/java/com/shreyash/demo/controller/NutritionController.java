package com.shreyash.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shreyash.demo.dto.NutritionLogRequest;
import com.shreyash.demo.dto.NutritionLogResponse;
import com.shreyash.demo.service.INutritionLogService;

@RestController
@RequestMapping("/api/nutrition")
public class NutritionController {

	@Autowired
	private INutritionLogService nutritionLogService;
	
	@PostMapping("/log")
	public ResponseEntity<NutritionLogResponse> logNutrition(NutritionLogRequest req){
		return ResponseEntity.ok(nutritionLogService.logNutrition(req));
	}
	
	@GetMapping("/history")
	public ResponseEntity<List<NutritionLogResponse>> getHistory(){
		return ResponseEntity.ok(nutritionLogService.getHistory());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteLog(@PathVariable Long id){
		return ResponseEntity.ok(nutritionLogService.delete(id));
	}
}
