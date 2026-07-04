package com.shreyash.demo.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class HydrationLogRequest {

    @Positive
    private Double amountMl;
}