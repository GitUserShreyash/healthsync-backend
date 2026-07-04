package com.shreyash.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shreyash.demo.dto.DashBoardResponse;
import com.shreyash.demo.service.DashboardService;

@RestController
@RequestMapping("/api")
public class DashboardController {
	
	@Autowired
	private DashboardService dashboardService;
	
	@GetMapping("/dashboard")
	public ResponseEntity<DashBoardResponse> getDashboard(){
		return ResponseEntity.ok(dashboardService.getDashboard());
	}
}
