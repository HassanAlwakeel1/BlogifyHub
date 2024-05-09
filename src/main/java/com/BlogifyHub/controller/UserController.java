package com.BlogifyHub.controller;

import com.BlogifyHub.model.DTO.UserDTO;
import com.BlogifyHub.model.DTO.UserProfileDTO;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable(value = "id") Long userId){
        return userService.getUserById(userId);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserProfileDTO> updateUserProfile(@RequestBody UserProfileDTO userProfileDTO,
                                                            @PathVariable(value = "id") Long userId){
        return userService.updateUserProfile(userProfileDTO, userId);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") Long userId){
        return userService.deleteUser(userId);
    }
}
