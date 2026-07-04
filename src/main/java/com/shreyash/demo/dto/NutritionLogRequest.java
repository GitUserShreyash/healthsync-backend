package com.shreyash.demo.dto;

import lombok.Data;

@Data
public class NutritionLogRequest {

    private String mealType;
    private String foodName;

    private Double quantity;
    private String unit;

    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    private Double fiber;
}
