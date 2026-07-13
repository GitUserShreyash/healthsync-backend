package com.shreyash.demo.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HydrationHistoryResponse {

    private LocalDate date;
    private Integer totalAmountMl;
}
