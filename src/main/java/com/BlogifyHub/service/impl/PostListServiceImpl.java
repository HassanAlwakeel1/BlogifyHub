package com.BlogifyHub.service.impl;

import com.BlogifyHub.exception.ResourceNotFoundException;
import com.BlogifyHub.model.DTO.PostDTO;
import com.BlogifyHub.model.DTO.PostListDTO;
import com.BlogifyHub.model.DTO.ProfileResponseDTO;
import com.BlogifyHub.model.entity.Post;
import com.BlogifyHub.model.entity.PostList;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.model.mapper.PostListMapper;
import com.BlogifyHub.model.mapper.PostMapper;
import com.BlogifyHub.repository.PostListRepository;
import com.BlogifyHub.repository.PostRepository;
import com.BlogifyHub.repository.UserRepository;
import com.BlogifyHub.service.PostListService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostListServiceImpl implements PostListService {

    private PostListMapper postListMapper;
    private PostListRepository postListRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;

    private PostMapper postMapper;

    public PostListServiceImpl(PostListMapper postListMapper,
                               PostListRepository postListRepository,
                               UserRepository userRepository,
                               PostRepository postRepository,
                               PostMapper postMapper) {
        this.postListMapper = postListMapper;
        this.postListRepository = postListRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    public PostListDTO createList(long userId, PostListDTO postListDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
        PostList postList = postListMapper.mapToEntity(postListDTO);
        postList.setUser(user);
        PostList createdList = postListRepository.save(postList);
        PostListDTO listDTO = postListMapper.mapToDTO(createdList);
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

    @Override
    public PostListDTO savePost(long userId, long listId, long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("user","id",userId));
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("post","id",postId));
        PostList postList = postListRepository.findById(listId)
                .orElseThrow(()-> new ResourceNotFoundException("postList","id",listId));

        Set<Post> listPosts = postList.getPosts();
        if (listPosts.equals(null)){
            listPosts = new HashSet<>();
            listPosts.add(post);
        }else listPosts.add(post);

        postList.setPosts(listPosts);
        postListRepository.save(postList);

        PostListDTO postListDTO = postListMapper.mapToDTO(postList);
        //This is for owner of the List
        ProfileResponseDTO listOwnerProfile = new ProfileResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBio(),
                user.getProfilePictureURL()
        );
        postListDTO.setProfileResponseDTO(listOwnerProfile);

        Set<PostDTO> postDTOSet = listPosts.stream()
                .map(postEntity -> {
                    User postUser = postEntity.getUser();
                    PostDTO postDTO = postMapper.mapToDTO(postEntity);
                    ProfileResponseDTO postOwnerProfile = new ProfileResponseDTO(
                            postUser.getId(),
                            postUser.getFirstName(),
                            postUser.getLastName(),
                            postUser.getBio(),
                            postUser.getProfilePictureURL()
                    );
                    postDTO.setProfileResponseDTO(postOwnerProfile);
                    return postDTO;
                })
                .collect(Collectors.toSet());

        postListDTO.setPostDTO(postDTOSet);
        return postListDTO;
    }
}
