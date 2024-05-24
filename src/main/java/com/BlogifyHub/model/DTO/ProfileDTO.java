package com.BlogifyHub.model.DTO;

import lombok.Data;

@Data
public class ProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePhotoURL;
}
