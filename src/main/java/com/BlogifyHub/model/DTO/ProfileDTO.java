package com.BlogifyHub.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePhotoURL;
}
