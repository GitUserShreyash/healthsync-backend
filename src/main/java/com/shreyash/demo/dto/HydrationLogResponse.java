package com.shreyash.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HydrationLogResponse {

    private Long id;
    private Double amountMl;
    private LocalDateTime loggedAt;
}
