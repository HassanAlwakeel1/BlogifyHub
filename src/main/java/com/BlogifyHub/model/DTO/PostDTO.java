package com.BlogifyHub.model.DTO;

import com.BlogifyHub.model.entity.enums.PostType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    private ProfileResponseDTO profileResponseDTO;

    private Set<CommentDTO> comments;

    private Long categoryId;

    private PostType postType;

}
