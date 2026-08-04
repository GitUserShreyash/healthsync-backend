package com.shreyash.demo.dto.response.progress;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeightDataPointDto {
	private LocalDate date;

    private Double weight;
}
