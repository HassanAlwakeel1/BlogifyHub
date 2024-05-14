package com.BlogifyHub.model.mapper.impl;

import com.BlogifyHub.model.DTO.CommentDTO;
import com.BlogifyHub.model.entity.Comment;
import com.BlogifyHub.model.mapper.CommentMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CommentMapperImpl implements CommentMapper {
    private ModelMapper mapper;

    public CommentMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public Comment mapToEntity(CommentDTO commentDTO){
        Comment comment = mapper.map(commentDTO,Comment.class);
        return comment;
    }
    @Override
    public CommentDTO mapToDTO(Comment comment){
        CommentDTO commentDTO = mapper.map(comment,CommentDTO.class);
        return commentDTO;
    }
}
