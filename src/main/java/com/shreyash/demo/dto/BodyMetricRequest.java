package com.shreyash.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BodyMetricRequest {

    @NotNull
    private Double weightKg;

    @NotNull
    private Double heightCm;
}