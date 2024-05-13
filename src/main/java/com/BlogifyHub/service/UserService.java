package com.BlogifyHub.service;

import com.BlogifyHub.model.DTO.*;
import com.BlogifyHub.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();
    ResponseEntity<UserDTO> getUserById(Long userId);

    ResponseEntity<ProfileResponseDTO> updateUserProfile(UserProfileDTO userProfileDTO, Long userId);

    ResponseEntity<String> deleteUser(Long userId);

    ResponseEntity<List<CustomUserDTO>> getAllUsers();

    ResponseEntity<String> changePassword(ChangePasswordDTO changePasswordDTO, Long userId);
}
