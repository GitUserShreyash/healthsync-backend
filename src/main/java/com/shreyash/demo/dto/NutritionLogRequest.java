package com.shreyash.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class NutritionLogRequest {

    @NotBlank(message = "Meal type is required")
    private String mealType;

    @NotBlank(message = "Food name is required")
    private String foodName;

    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity cannot be negative")
    private Double quantity;

    @NotNull(message = "Calories are required")
    @PositiveOrZero(message = "Calories cannot be negative")
    private Double calories;

    @NotNull(message = "Protein is required")
    @PositiveOrZero(message = "Protein cannot be negative")
    private Double protein;

    @NotNull(message = "Carbs are required")
    @PositiveOrZero(message = "Carbs cannot be negative")
    private Double carbs;

    @NotNull(message = "Fat is required")
    @PositiveOrZero(message = "Fat cannot be negative")
    private Double fat;

    @NotNull(message = "Fiber is required")
    @PositiveOrZero(message = "Fiber cannot be negative")
    private Double fiber;
}