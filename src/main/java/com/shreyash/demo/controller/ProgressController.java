package com.shreyash.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shreyash.demo.dto.response.progress.ProgressResponse;
import com.shreyash.demo.service.impl.ProgressService;


@RestController
@RequestMapping("/api/progress")
public class ProgressController {
	private final ProgressService progressService;
	
	public ProgressController(ProgressService progressService) {
		this.progressService=progressService;
	}
	
	@GetMapping("/{days}")
	public ResponseEntity<ProgressResponse> getProgress(@PathVariable Integer days){
		
		if (days != 7 && days != 30 && days != 90) {
	        throw new IllegalArgumentException("Days must be 7, 30 or 90");
	    }
		
		return ResponseEntity.ok(progressService.getProgress(days));
	}
	
}
