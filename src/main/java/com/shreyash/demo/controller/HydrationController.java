package com.shreyash.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shreyash.demo.dto.HydrationHistoryResponse;
import com.shreyash.demo.dto.HydrationLogRequest;
import com.shreyash.demo.dto.HydrationLogResponse;
import com.shreyash.demo.service.IHydrationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hydration")
public class HydrationController {
	
	@Autowired
	private IHydrationService hydrationService;
	
	@PostMapping()
	public ResponseEntity<HydrationLogResponse> logHydration(@Valid @RequestBody HydrationLogRequest req){
		return ResponseEntity.ok(hydrationService.logWater(req));
	}
	
	@GetMapping("/history")
	public ResponseEntity<List<HydrationHistoryResponse>> getHistory(){
		return ResponseEntity.ok(hydrationService.getHistory());
	}
	
	@DeleteMapping("/{logId}")
	public ResponseEntity<String> deleteHydrationLog(@PathVariable Long logId){
		return ResponseEntity.ok(hydrationService.deleteHydrationLog(logId));
	}
	
	@GetMapping()
	public ResponseEntity<List<HydrationLogResponse>> getTodaysLogs(){
		return ResponseEntity.ok(hydrationService.getTodaysLog());
	}
}
