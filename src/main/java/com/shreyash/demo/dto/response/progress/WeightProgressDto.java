package com.shreyash.demo.dto.response.progress;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeightProgressDto {
	private Double currentWeight;

    private Double startingWeight;

    private Double highestWeight;

    private Double lowestWeight;

    private Double weightChange;

    private List<WeightDataPointDto> history;
}
