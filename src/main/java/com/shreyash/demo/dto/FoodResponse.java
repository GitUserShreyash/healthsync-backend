package com.shreyash.demo.dto;

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
    private String mealType;

    private Double caloriesPer100g;
    private Double proteinG;
    private Double carbsG;
    private Double fatG;
}
