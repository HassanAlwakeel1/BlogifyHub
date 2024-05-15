package com.BlogifyHub.service;

import com.BlogifyHub.model.DTO.PostDTO;
import com.BlogifyHub.model.DTO.PostListDTO;

public interface PostListService {
    PostListDTO createList(long userId, PostListDTO postListDTO);

    PostListDTO savePost(long userId,long listId, long postId);
}
