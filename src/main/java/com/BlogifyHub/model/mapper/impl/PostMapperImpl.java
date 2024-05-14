package com.BlogifyHub.model.mapper.impl;

import com.BlogifyHub.model.DTO.PostDTO;
import com.BlogifyHub.model.DTO.UserPostDTO;
import com.BlogifyHub.model.entity.Post;
import com.BlogifyHub.model.mapper.PostMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PostMapperImpl implements PostMapper {
    private ModelMapper mapper;

    public PostMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public PostDTO mapToDTO(Post post){
        PostDTO postDto = mapper.map(post,PostDTO.class);
        return postDto;
    }

    @Override
    public Post mapToEntity(PostDTO postDTO){
        Post post = mapper.map(postDTO,Post.class);
        return post;
    }

    @Override
    public UserPostDTO mapPostToUserPostDTO(Post post){
        return mapper.map(post,UserPostDTO.class);
    }

}
