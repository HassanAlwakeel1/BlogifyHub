package com.BlogifyHub.model.mapper;

import com.BlogifyHub.model.DTO.CommentDTO;
import com.BlogifyHub.model.entity.Comment;

public interface CommentMapper {
    public Comment mapToEntity(CommentDTO commentDTO);
    public CommentDTO mapToDTO(Comment comment);
}
