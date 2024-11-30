package com.BlogifyHub.model.mapper.impl;

import com.BlogifyHub.model.DTO.CustomUserDTO;
import com.BlogifyHub.model.DTO.ProfileDTO;
import com.BlogifyHub.model.DTO.ProfileResponseDTO;
import com.BlogifyHub.model.DTO.UserDTO;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.model.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperImplTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserMapperImpl userMapperImpl;

    private User user;
    private UserDTO userDTO;
    private ProfileResponseDTO profileResponseDTO;
    private CustomUserDTO customUserDTO;
    private ProfileDTO profileDTO;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        // Initializing the test data
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBio("Bio text");
        user.setProfilePictureURL("profileURL");
        user.setRole(Role.USER);

        userDTO = new UserDTO(
                1L,
                "John",
                "Doe",
                "Bio text",
                "profileURL",
                Role.USER,
                null);

        profileResponseDTO = new ProfileResponseDTO(
                1L,
                "John",
                "Doe",
                "Bio text",
                "profileURL",
                0);

        customUserDTO = new CustomUserDTO(
                1L,
                "John",
                "Doe",
                "Bio text",
                Role.USER);

        profileDTO = new ProfileDTO(
                1L,
                "John",
                "Doe",
                "Bio text",
                "profileURL");

        // Mocking the behavior of the ModelMapper
        Mockito.when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        Mockito.when(modelMapper.map(user, ProfileResponseDTO.class)).thenReturn(profileResponseDTO);
        Mockito.when(modelMapper.map(user, CustomUserDTO.class)).thenReturn(customUserDTO);
        Mockito.when(modelMapper.map(user, ProfileDTO.class)).thenReturn(profileDTO);
    }

    @Test
    void userToUserDTO() {
        UserDTO result = userMapperImpl.userToUserDTO(user);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getBio(), result.getBio());
        assertEquals(user.getProfilePictureURL(), result.getProfilePhotoURL());
        assertEquals(user.getRole(), result.getRole());

        Mockito.verify(modelMapper, Mockito.times(1)).map(user, UserDTO.class);
    }

    @Test
    void userToUpdatedProfileDTO() {
        ProfileResponseDTO result = userMapperImpl.userToUpdatedProfileDTO(user);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getBio(), result.getBio());
        assertEquals(user.getProfilePictureURL(), result.getProfilePictureURL());
        assertEquals(user.getFollowersNumber(), result.getFollowersNumber());

        Mockito.verify(modelMapper, Mockito.times(1)).map(user, ProfileResponseDTO.class);
    }

    @Test
    void userToCustomUserDTO() {
        CustomUserDTO result = userMapperImpl.userToCustomUserDTO(user);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getBio(), result.getBio());
        assertEquals(user.getRole(), result.getRole());

        Mockito.verify(modelMapper, Mockito.times(1)).map(user, CustomUserDTO.class);
    }

    @Test
    void userToProfileDTO() {
        ProfileDTO result = userMapperImpl.userToProfileDTO(user);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getBio(), result.getBio());
        assertEquals(user.getProfilePictureURL(), result.getProfilePhotoURL());

        Mockito.verify(modelMapper, Mockito.times(1)).map(user, ProfileDTO.class);
    }
}