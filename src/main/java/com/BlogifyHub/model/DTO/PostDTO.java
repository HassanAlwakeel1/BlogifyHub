package com.BlogifyHub.model.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDTO {

    private Long id;

    @NotEmpty
    @Size(min = 2, message = "Post title should have at least 2 characters")
    private String title;

    @NotEmpty
    @Size(min = 10, message = "Post title should have at least 10 characters")
    private String description;

    @NotEmpty
    private String content;

    private Set<CommentDTO> comments;

    private Long categoryId;

}
