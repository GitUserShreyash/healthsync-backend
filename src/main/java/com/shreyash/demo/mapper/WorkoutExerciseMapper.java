package com.shreyash.demo.mapper;

import org.mapstruct.Mapper;

import com.shreyash.demo.dto.workout.WorkoutExerciseResponse;
import com.shreyash.demo.model.WorkoutExercise;

@Mapper(componentModel = "spring")
public interface WorkoutExerciseMapper {
	WorkoutExerciseResponse toDto(WorkoutExercise exercise);

}
