package com.BlogifyHub.model.mapper;

import com.BlogifyHub.model.DTO.PostDTO;
import com.BlogifyHub.model.DTO.UserPostDTO;
import com.BlogifyHub.model.entity.Post;

public interface PostMapper {
    public PostDTO mapToDTO(Post post);

    public Post mapToEntity(PostDTO postDTO);

    public UserPostDTO mapPostToUserPostDTO(Post post);
}
