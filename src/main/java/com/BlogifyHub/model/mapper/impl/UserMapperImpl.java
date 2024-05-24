package com.BlogifyHub.model.mapper.impl;

import com.BlogifyHub.model.DTO.CustomUserDTO;
import com.BlogifyHub.model.DTO.ProfileDTO;
import com.BlogifyHub.model.DTO.ProfileResponseDTO;
import com.BlogifyHub.model.DTO.UserDTO;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.model.mapper.UserMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImpl implements UserMapper {
    private ModelMapper mapper;

    public UserMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserDTO userToUserDTO(User user){
        return mapper.map(user,UserDTO.class);
    }
    @Override
    public ProfileResponseDTO userToUpdatedProfileDTO(User user){
        return mapper.map(user,ProfileResponseDTO.class);
    }
    @Override
    public CustomUserDTO userToCustomUserDTO(User user){
        return mapper.map(user,CustomUserDTO.class);
    }

    @Override
    public ProfileDTO userToProfileDTO(User user) {
        return mapper.map(user,ProfileDTO.class);
    }


}
