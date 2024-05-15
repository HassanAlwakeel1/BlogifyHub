package com.BlogifyHub.controller;

import com.BlogifyHub.model.DTO.PostListDTO;
import com.BlogifyHub.model.entity.PostList;
import com.BlogifyHub.service.PostListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/users/{id}/post-lists")
public class PostListController {
    private PostListService postListService;

    public PostListController(PostListService postListService) {
        this.postListService = postListService;
    }

    @PostMapping
    public ResponseEntity<PostListDTO> createList(@PathVariable(name = "id") Long userId,
                                             @RequestBody PostListDTO postListDTO){
        PostListDTO listDTO = postListService.createList(userId,postListDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(listDTO);
    }
    @PostMapping("/{listId}/posts/{postId}")
    public PostListDTO savePost(@PathVariable(name = "id") Long userId,
                                @PathVariable(name = "listId") long listId,
                                @PathVariable(name= "postId") long postId){
        return postListService.savePost(userId,listId,postId);
    }

    @DeleteMapping("/{listId}")
    public String deleteList(@PathVariable(name = "id") Long userId,
                             @PathVariable(name = "listId") long listId){
        return postListService.deleteList(userId,listId);
    }

    @GetMapping
    public Set<PostListDTO> getUserLists(@PathVariable(name = "id") Long userId){
        Set<PostListDTO> postListDTOSet = postListService.getUserLists(userId);
        return postListDTOSet;
    }

    @GetMapping("/{listId}")
    public PostListDTO getListById(@PathVariable(name = "listId") Long listId,
                                   @PathVariable(name = "id") Long userId){
        PostListDTO postListDTO = postListService.getListById(listId);
        return postListDTO;
    }
}
