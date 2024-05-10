package com.BlogifyHub.service.impl;

import com.BlogifyHub.exception.ResourceNotFoundException;
import com.BlogifyHub.model.DTO.*;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.repository.UserRepository;
import com.BlogifyHub.service.CloudinaryImageService;
import com.BlogifyHub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    private final CloudinaryImageService cloudinaryImageService;

    private final PasswordEncoder passwordEncoder;



    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                return userRepository.findByEmail(email)
                        .orElseThrow(()-> new UsernameNotFoundException("User not found"));
            }
        };
    }

    public ResponseEntity<UserDTO> getUserById(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
        UserDTO userDTO = userToUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @Override
    public ResponseEntity<UpdatedProfileDTO> updateUserProfile(UserProfileDTO userProfileDTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
        user.setFirstName(userProfileDTO.getFirstName());
        user.setLastName(userProfileDTO.getLastName());
        user.setBio(userProfileDTO.getBio());
        MultipartFile photo = userProfileDTO.getProfilePicture();
        if (photo != null && !photo.isEmpty()) {
            Map uploadImageMap = cloudinaryImageService.upload(photo);
            String photoUrl = (String)uploadImageMap.get("secure_url");
            user.setProfilePictureURL(photoUrl);
        }
        userRepository.save(user);
        UpdatedProfileDTO updatedUserProfileDTO = userToUpdatedProfileDTO(user);
        return ResponseEntity.ok(updatedUserProfileDTO);
    }

    @Override
    public ResponseEntity<String> deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
        userRepository.delete(user);
        return ResponseEntity.ok("User deleted successfully");
    }

    @Override
    public ResponseEntity<List<CustomUserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<CustomUserDTO> customUserDTOS = new ArrayList<>();
        for (User user : users) {
            CustomUserDTO customUserDTO = userToCustomUserDTO(user);
            customUserDTOS.add(customUserDTO);
        }
        return ResponseEntity.ok(customUserDTOS);
    }

    @Override
    public ResponseEntity<String> changePassword(ChangePasswordDTO changePasswordDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
        String userPassword = user.getPassword();
        String oldPassword = changePasswordDTO.getOldPassword();
        String newPassword = changePasswordDTO.getNewPassword();
        String newPasswordConfirmation = changePasswordDTO.getNewPasswordConfirmation();
        if (newPassword.equals(newPasswordConfirmation) && passwordEncoder.matches(oldPassword, userPassword)){
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return ResponseEntity.ok("Password changed successfully!");
        }else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad credentials");
    }

    private UserDTO userToUserDTO(User user){
        return mapper.map(user,UserDTO.class);
    }

    private UpdatedProfileDTO userToUpdatedProfileDTO(User user){
        return mapper.map(user,UpdatedProfileDTO.class);
    }

    private CustomUserDTO userToCustomUserDTO(User user){
        return mapper.map(user,CustomUserDTO.class);
    }

}


