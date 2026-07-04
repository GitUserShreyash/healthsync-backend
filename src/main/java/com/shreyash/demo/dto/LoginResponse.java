package com.shreyash.demo.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginResponse {

	@NonNull
	private String token;
	
	@NonNull
	private String role;
}
