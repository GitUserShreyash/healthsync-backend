package com.shreyash.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="foods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
