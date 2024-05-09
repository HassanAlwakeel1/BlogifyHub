package com.BlogifyHub.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String Bio;
    private String ProfilePictureURL;
}
