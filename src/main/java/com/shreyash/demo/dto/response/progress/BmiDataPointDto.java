package com.shreyash.demo.dto.response.progress;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BmiDataPointDto {
	private LocalDate date;

    private Double bmi;
}
