package com.shreyash.demo.mapper;

import org.mapstruct.Mapper;

import com.shreyash.demo.dto.NutritionLogRequest;
import com.shreyash.demo.dto.NutritionLogResponse;
import com.shreyash.demo.model.NutritionLog;

@Mapper(componentModel = "spring")
public interface NutritionLogMapper {
	NutritionLogResponse toDto(NutritionLog log);
	NutritionLog toEntity(NutritionLogRequest req);
}
