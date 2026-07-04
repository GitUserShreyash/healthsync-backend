package com.shreyash.demo.mapper;

import org.mapstruct.Mapper;

import com.shreyash.demo.dto.HydrationLogRequest;
import com.shreyash.demo.dto.HydrationLogResponse;
import com.shreyash.demo.model.HydrationLog;

@Mapper(componentModel = "spring")
public interface HydrationLogMapper {
	HydrationLogResponse toDto(HydrationLog log);
	HydrationLog toEntity(HydrationLogRequest req);
}
