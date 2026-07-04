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
@Table(name = "body_metrics_table")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BodyMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double weightKg;

    private Double heightCm;

    private Double bmi;

    private String bmiCategory;

    private Double bodyFat;

    private String bodyFatCategory;

    private LocalDateTime recordedAt;
}
