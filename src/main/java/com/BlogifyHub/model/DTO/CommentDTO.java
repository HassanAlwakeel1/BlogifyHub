package com.BlogifyHub.model.DTO;

import lombok.Data;

@Data
public class CommentDTO {
    private long id;
    private String name;
    private String email;
    private String body;
}
