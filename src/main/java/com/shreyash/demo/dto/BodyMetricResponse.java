package com.shreyash.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BodyMetricResponse {

    private Long id;
    private Double weightKg;
    private Double heightCm;
    private Double bmi;
    private String bmiCategory;
    private Double bodyFat;
    private String bodyFatCategory;
    private LocalDateTime recordedAt;
}
