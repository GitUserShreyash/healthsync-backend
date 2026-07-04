package com.shreyash.demo.mapper;

import org.mapstruct.Mapper;

import com.shreyash.demo.dto.FoodResponse;
import com.shreyash.demo.model.Food;

@Mapper(componentModel = "spring")
public interface FoodMapper {
	FoodResponse toDto(Food food);
}
