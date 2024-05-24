package com.BlogifyHub.service;


import org.springframework.security.core.Authentication;

public interface FollowService {
    void follow(Long userId, Authentication authentication);

    void unFollow(Long userId, Authentication authentication);
}
