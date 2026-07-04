package com.shreyash.demo.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class VerifyEmailRequest {

    @NonNull
    private String email;

    @NonNull
    private String otp;
}