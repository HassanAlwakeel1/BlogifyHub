package com.BlogifyHub.model.DTO;

import com.BlogifyHub.model.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String bio;
    private Role role;
}
