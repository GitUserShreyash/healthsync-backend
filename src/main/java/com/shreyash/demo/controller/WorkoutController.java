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

import com.shreyash.demo.dto.WorkoutLogRequest;
import com.shreyash.demo.dto.WorkoutLogResponse;
import com.shreyash.demo.dto.WorkoutPlanResponse;
import com.shreyash.demo.dto.WorkoutSummaryResponse;
import com.shreyash.demo.service.IWorkoutPlanService;
import com.shreyash.demo.service.IWorkoutService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/workout")
public class WorkoutController {
	@Autowired
	private IWorkoutPlanService workoutPlanService;
	
	@Autowired
	private IWorkoutService workoutService;
	
	@GetMapping("/plan")
	public ResponseEntity<List<WorkoutPlanResponse>> getPlan() {
	    return ResponseEntity.ok(workoutPlanService.getWeeklyPlan());
	}
	
	@PostMapping("/log")
    public ResponseEntity<WorkoutLogResponse> logWorkout(@Valid @RequestBody WorkoutLogRequest request) {
        return ResponseEntity.ok(workoutService.logWorkout(request));
    }
	
	@GetMapping("/history")
	public ResponseEntity<List<WorkoutLogResponse>> getHistory(){
		return ResponseEntity.ok(workoutService.getHistory());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteWorkout(@PathVariable Long id){
		return ResponseEntity.ok(workoutService.delete(id));
	}
	
	@GetMapping("/summary")
	public ResponseEntity<WorkoutSummaryResponse> getWorkoutSummary() {
	    return ResponseEntity.ok(workoutService.getWorkoutSummary());
	}

	@GetMapping("/today")
	public ResponseEntity<WorkoutPlanResponse> getTodayWorkout() {
	    return ResponseEntity.ok(workoutPlanService.getTodayWorkout());
	}
	
	@GetMapping("/today/status")
	public ResponseEntity<Boolean> getTodaysLoggedWorkout(){
		return ResponseEntity.ok(workoutService.getTodayStatus());
	}
}
