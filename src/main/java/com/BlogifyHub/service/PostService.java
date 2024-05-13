package com.BlogifyHub.service;

import com.BlogifyHub.model.DTO.PostDTO;
import com.BlogifyHub.model.DTO.PostResponseDTO;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDTO, Long userId);

    PostResponseDTO getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDirection);

    PostDTO getPostById(long id);

    PostDTO updatePost(PostDTO postDTO, long userId, long postId);

    void deletePostById(long userId, long postId);

    List<PostDTO> getPostsByCategory(Long categoryId);
}
