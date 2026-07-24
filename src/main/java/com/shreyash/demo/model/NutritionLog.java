package com.shreyash.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nutrition_logs_table")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NutritionLog {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private String mealType;

    private String foodName;

    private Double quantity;

    private Double calories;

    private Double protein;

    private Double carbs;

    private Double fat;

    private Double fiber;

    private LocalDateTime loggedAt;
}
