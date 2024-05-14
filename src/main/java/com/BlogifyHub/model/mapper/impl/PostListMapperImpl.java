package com.BlogifyHub.model.mapper.impl;

import com.BlogifyHub.model.DTO.PostListDTO;
import com.BlogifyHub.model.entity.PostList;
import com.BlogifyHub.model.mapper.PostListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PostListMapperImpl implements PostListMapper {

    private ModelMapper mapper;

    public PostListMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public PostListDTO mapToDTO(PostList postList){
        return mapper.map(postList,PostListDTO.class);
    }
    @Override
    public PostList mapToEntity(PostListDTO postListDTO){
        return mapper.map(postListDTO,PostList.class);
    }
}
