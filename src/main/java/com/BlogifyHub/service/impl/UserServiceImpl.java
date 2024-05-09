package com.BlogifyHub.service.impl;

import com.BlogifyHub.exception.ResourceNotFoundException;
import com.BlogifyHub.model.DTO.CustomUserDTO;
import com.BlogifyHub.model.DTO.UserDTO;
import com.BlogifyHub.model.DTO.UserProfileDTO;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.repository.UserRepository;
import com.BlogifyHub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper mapper;


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
    public ResponseEntity<UserProfileDTO> updateUserProfile(UserProfileDTO userProfileDTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
        user.setFirstName(userProfileDTO.getFirstName());
        user.setLastName(userProfileDTO.getLastName());
        user.setBio(userProfileDTO.getBio());
        userRepository.save(user);
        UserProfileDTO updatedUserProfileDTO = userToUserProfileDTO(user);
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

    private UserDTO userToUserDTO(User user){
        return mapper.map(user,UserDTO.class);
    }

    private UserProfileDTO userToUserProfileDTO(User user){
        return mapper.map(user,UserProfileDTO.class);
    }

    private CustomUserDTO userToCustomUserDTO(User user){
        return mapper.map(user,CustomUserDTO.class);
    }

}


