package com.shreyash.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodResponse {

    private Long id;

    private String foodName;

    private String category;

    private Double caloriesPer100g;

    private Double proteinPer100g;

    private Double carbsPer100g;

    private Double fatPer100g;

    private Double fiberPer100g;

    private Double sugarPer100g;

    private Double sodiumPer100g;
}