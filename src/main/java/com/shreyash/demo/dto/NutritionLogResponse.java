package com.shreyash.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NutritionLogResponse {

    private Long id;

    private String mealType;
    private String foodName;

    private Double quantity;
    private String unit;

    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    private Double fiber;

    private LocalDateTime loggedAt;
}