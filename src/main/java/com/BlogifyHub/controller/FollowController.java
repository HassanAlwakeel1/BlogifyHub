package com.BlogifyHub.controller;

import com.BlogifyHub.service.FollowService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/follow")
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
}