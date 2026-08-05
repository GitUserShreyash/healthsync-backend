package com.shreyash.demo.dto.response.progress;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor

public class BmiProgressDto {
	private Double currentBmi;

    private String bmiCategory;

    private List<BmiDataPointDto> history;
}
