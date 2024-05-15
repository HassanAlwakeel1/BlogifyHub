package com.BlogifyHub.service;

import com.BlogifyHub.model.DTO.PostDTO;
import com.BlogifyHub.model.DTO.PostListDTO;

import java.util.Set;

public interface PostListService {
    PostListDTO createList(long userId, PostListDTO postListDTO);

    PostListDTO savePost(long userId,long listId, long postId);

    String deleteList(Long userId, long listId);

    Set<PostListDTO> getUserLists(Long userId);

    PostListDTO getListById(Long listId);
}
