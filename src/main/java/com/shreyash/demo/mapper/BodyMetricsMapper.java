package com.shreyash.demo.mapper;

import org.mapstruct.Mapper;

import com.shreyash.demo.dto.BodyMetricResponse;
import com.shreyash.demo.model.BodyMetrics;

@Mapper(componentModel = "spring")
public interface BodyMetricsMapper {
	BodyMetricResponse toDto(BodyMetrics metrics);
}
