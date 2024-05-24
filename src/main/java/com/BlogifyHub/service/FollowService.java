package com.BlogifyHub.service;


import com.BlogifyHub.model.DTO.ProfileDTO;
import com.BlogifyHub.model.DTO.ProfileResponseDTO;
import com.BlogifyHub.model.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface FollowService {
    void follow(Long userId, Authentication authentication);

    void unFollow(Long userId, Authentication authentication);

    List<ProfileResponseDTO> getFollowers(Long userId);
}
