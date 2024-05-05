package com.BlogifyHub.model.DTO;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String password;
}