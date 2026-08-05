package com.shreyash.demo.dto.response.progress;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CaloriesDataPointDto {
	private LocalDate date;

    private Double calories;
}
