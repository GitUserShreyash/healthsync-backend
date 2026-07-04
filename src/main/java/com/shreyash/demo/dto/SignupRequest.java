package com.shreyash.demo.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class SignupRequest {
	@NonNull
    private String username;

    @NonNull
    private String email;

    @NonNull
    private String password;
}
