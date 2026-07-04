package com.shreyash.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shreyash.demo.dto.BodyMetricResponse;
import com.shreyash.demo.service.IBodyMetricsService;

@RestController
@RequestMapping("/api/metrics")
public class BodyMetricsController {
	@Autowired
	private IBodyMetricsService metricsService;
	
	@GetMapping("/history")
	public ResponseEntity<List<BodyMetricResponse>> getHistory(){
		return ResponseEntity.ok(metricsService.getHistory());
	}
}
