package com.shreyash.demo.dto;

import java.time.DayOfWeek;
import java.util.List;

import com.shreyash.demo.dto.workout.WorkoutExerciseResponse;
import com.shreyash.demo.enums.WorkoutType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPlanResponse {

    private Long id;
    private DayOfWeek day;
    private WorkoutType workoutType;
    private Integer targetDurationMinutes;
    private String description;
    private List<WorkoutExerciseResponse> exercises;
}