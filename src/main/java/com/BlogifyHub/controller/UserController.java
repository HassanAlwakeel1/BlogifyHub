package com.BlogifyHub.controller;

import com.BlogifyHub.model.DTO.*;
import com.BlogifyHub.service.PostService;
import com.BlogifyHub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;
    private PostService postService;


    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
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
    public ResponseEntity<ProfileResponseDTO> updateUserProfile(@ModelAttribute UserProfileDTO userProfileDTO,
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

    @PostMapping("/{userId}/posts")
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO, @PathVariable Long userId){
        return new ResponseEntity<>(postService.createPost(postDTO,userId), HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/posts/{postId}")
    public ResponseEntity<PostDTO> updatePost(@Valid @RequestBody PostDTO postDTO,
                                              @PathVariable(name = "userId")long userId,
                                              @PathVariable(name = "postId") long postId
                                              ){
        PostDTO postResponse = postService.updatePost(postDTO,userId,postId);
        return new ResponseEntity<>(postResponse,HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "userId") long userId,
                                             @PathVariable(name = "postId") long postId){
        postService.deletePostById(userId,postId);
        return new ResponseEntity<>("Post entity deleted successfully.",HttpStatus.OK);
    }
}
