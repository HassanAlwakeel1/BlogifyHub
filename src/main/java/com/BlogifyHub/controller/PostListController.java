package com.BlogifyHub.controller;

import com.BlogifyHub.model.DTO.PostListDTO;
import com.BlogifyHub.model.entity.PostList;
import com.BlogifyHub.service.PostListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
