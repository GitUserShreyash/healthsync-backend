package com.shreyash.demo.mapper;

import org.mapstruct.Mapper;

import com.shreyash.demo.dto.WorkoutPlanResponse;
import com.shreyash.demo.model.WorkoutPlan;

@Mapper(
	componentModel = "spring",
	uses = WorkoutExerciseMapper.class
)
public interface WorkoutPlanMapper {
	WorkoutPlanResponse toDto(WorkoutPlan plan);
	
}
