package com.BlogifyHub.model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDTO {
    private long id;

    @NotEmpty
    @Size(min = 10, message = "Comment body should contain at least 10 characters")
    private String body;

    private ProfileResponseDTO profileResponseDTO;
}
