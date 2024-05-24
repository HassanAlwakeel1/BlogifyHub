package com.BlogifyHub.model.mapper;

import com.BlogifyHub.model.DTO.CustomUserDTO;
import com.BlogifyHub.model.DTO.ProfileDTO;
import com.BlogifyHub.model.DTO.ProfileResponseDTO;
import com.BlogifyHub.model.DTO.UserDTO;
import com.BlogifyHub.model.entity.User;

public interface UserMapper {

    public UserDTO userToUserDTO(User user);

    public ProfileResponseDTO userToUpdatedProfileDTO(User user);

    public CustomUserDTO userToCustomUserDTO(User user);

    public ProfileDTO userToProfileDTO(User user);
}
