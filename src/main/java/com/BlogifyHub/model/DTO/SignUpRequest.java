package com.BlogifyHub.model.DTO;

import com.BlogifyHub.model.entity.enums.Role;
import lombok.Data;

@Data
public class SignUpRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Role role;
}