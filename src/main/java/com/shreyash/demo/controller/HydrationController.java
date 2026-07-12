package com.shreyash.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shreyash.demo.dto.HydrationLogRequest;
import com.shreyash.demo.dto.HydrationLogResponse;
import com.shreyash.demo.service.IHydrationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hydration")
public class HydrationController {
	
	@Autowired
	private IHydrationService hydrationService;
	
	@PostMapping("/log")
	public ResponseEntity<HydrationLogResponse> logHydration(@Valid @RequestBody HydrationLogRequest req){
		return ResponseEntity.ok(hydrationService.logWater(req));
	}
	
	@GetMapping("History")
	public ResponseEntity<List<HydrationLogResponse>> getHistory(){
		return ResponseEntity.ok(hydrationService.getHistory());
	}
}
