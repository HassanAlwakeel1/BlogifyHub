package com.BlogifyHub.model.mapper;

import com.BlogifyHub.model.DTO.PostListDTO;
import com.BlogifyHub.model.entity.PostList;

public interface PostListMapper {
    public PostListDTO mapToDTO(PostList postList);
    public PostList mapToEntity(PostListDTO postListDTO);
}
