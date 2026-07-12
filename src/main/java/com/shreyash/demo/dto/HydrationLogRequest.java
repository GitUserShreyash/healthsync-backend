package com.shreyash.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class HydrationLogRequest {

	@NotNull(message = "Water amount is required")
    @Positive(message = "Water amount must be greater than 0")
    private Double amountMl;
}