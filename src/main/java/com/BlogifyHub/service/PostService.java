package com.BlogifyHub.service;

import com.BlogifyHub.model.DTO.PostDTO;
import com.BlogifyHub.model.DTO.PostResponseDTO;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDTO, Long userId);

    PostResponseDTO getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDirection);

    PostDTO getPostById(long id);

    PostDTO updatePost(PostDTO postDTO,long id);
    void deletePostById(long id);

    List<PostDTO> getPostsByCategory(Long categoryId);
}
