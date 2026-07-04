package com.shreyash.demo.service;

import java.util.List;

import com.shreyash.demo.dto.WorkoutLogRequest;
import com.shreyash.demo.dto.WorkoutLogResponse;

public interface IWorkoutService {
	WorkoutLogResponse logWorkout(WorkoutLogRequest req);

    List<WorkoutLogResponse> getHistory();

    String delete(Long id);
}
