package com.BlogifyHub.controller;

import com.BlogifyHub.model.DTO.ProfileDTO;
import com.BlogifyHub.model.DTO.ProfileResponseDTO;
import com.BlogifyHub.service.FollowService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/follow")
@Tag(name = "Follow")
public class FollowController {
    private FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> followUser(@PathVariable(name = "id") Long userId, Authentication authentication) {
        followService.follow(userId, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unFollow(@PathVariable(name = "id") Long userId, Authentication authentication){
        followService.unFollow(userId, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ProfileResponseDTO>> getFollowers(@PathVariable(name = "id") Long userId){
        return ResponseEntity.ok(followService.getFollowers(userId));
    }

}