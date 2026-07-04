package com.shreyash.demo.mapper;

import org.mapstruct.Mapper;

import com.shreyash.demo.dto.WorkoutLogRequest;
import com.shreyash.demo.dto.WorkoutLogResponse;
import com.shreyash.demo.model.WorkoutLog;

@Mapper(componentModel = "spring")
public interface WorkoutLogMapper {
	WorkoutLogResponse toDto(WorkoutLog log);
	WorkoutLog toEntity(WorkoutLogRequest req);
}
