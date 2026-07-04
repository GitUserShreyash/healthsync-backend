package com.shreyash.demo.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class ForgotPasswordRequest {
	@NonNull
    private String email;
}
