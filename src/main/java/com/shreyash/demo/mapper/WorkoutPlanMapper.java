package com.shreyash.demo.mapper;

import org.mapstruct.Mapper;

import com.shreyash.demo.dto.WorkoutPlanResponse;
import com.shreyash.demo.model.WorkoutPlan;

@Mapper(componentModel = "spring")
public interface WorkoutPlanMapper {
	WorkoutPlanResponse toDto(WorkoutPlan plan);
	
}
