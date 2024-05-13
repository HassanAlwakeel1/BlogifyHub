package com.BlogifyHub.service.impl;

import com.BlogifyHub.exception.ResourceNotFoundException;
import com.BlogifyHub.model.DTO.PostListDTO;
import com.BlogifyHub.model.DTO.ProfileResponseDTO;
import com.BlogifyHub.model.entity.PostList;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.repository.PostListRepository;
import com.BlogifyHub.repository.UserRepository;
import com.BlogifyHub.service.PostListService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PostListServiceImpl implements PostListService {

    private ModelMapper mapper;
    private PostListRepository postListRepository;
    private UserRepository userRepository;

    public PostListServiceImpl(ModelMapper mapper, PostListRepository postListRepository, UserRepository userRepository) {
        this.mapper = mapper;
        this.postListRepository = postListRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PostListDTO createList(long userId, PostListDTO postListDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
        PostList postList = mapToEntity(postListDTO);
        postList.setUser(user);
        PostList createdList = postListRepository.save(postList);
        PostListDTO listDTO = mapToDTO(createdList);
        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBio(),
                user.getProfilePictureURL()
        );
        listDTO.setProfileResponseDTO(profileResponseDTO);
        return listDTO;
    }

    private PostListDTO mapToDTO(PostList postList){
        return mapper.map(postList,PostListDTO.class);
    }
    private PostList mapToEntity(PostListDTO postListDTO){
        return mapper.map(postListDTO,PostList.class);
    }
}
