package com.BlogifyHub.service;

import com.BlogifyHub.model.DTO.UserDTO;
import com.BlogifyHub.model.DTO.UserProfileDTO;
import com.BlogifyHub.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
    ResponseEntity<UserDTO> getUserById(Long userId);

    ResponseEntity<UserProfileDTO> updateUserProfile(UserProfileDTO userProfileDTO, Long userId);
}
