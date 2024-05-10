package com.BlogifyHub.controller;

import com.BlogifyHub.model.DTO.*;
import com.BlogifyHub.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<CustomUserDTO>> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable(value = "id") Long userId){
        return userService.getUserById(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdatedProfileDTO> updateUserProfile(@ModelAttribute UserProfileDTO userProfileDTO,
                                                               @PathVariable(value = "id") Long userId){
        return userService.updateUserProfile(userProfileDTO, userId);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") Long userId){
        return userService.deleteUser(userId);
    }
    @PutMapping("/{id}/password")
    public ResponseEntity<String> changePassword(@PathVariable(name = "id") Long userId,
                                                 @RequestBody ChangePasswordDTO changePasswordDTO){
        return userService.changePassword(changePasswordDTO, userId);
    }
}
