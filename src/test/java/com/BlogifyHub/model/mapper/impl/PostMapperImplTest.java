package com.BlogifyHub.model.mapper.impl;

import com.BlogifyHub.model.DTO.PostDTO;
import com.BlogifyHub.model.DTO.UserPostDTO;
import com.BlogifyHub.model.entity.Post;
import com.BlogifyHub.model.entity.enums.PostType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class PostMapperImplTest {
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PostMapperImpl postMapperImpl;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void mapToDTO() {
        // Prepare sample Post entity
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Post Title");
        post.setDescription("This is a description.");
        post.setContent("This is the content of the post.");
        post.setPostType(PostType.PUBLIC);

        // Create the expected PostDTO
        PostDTO expectedPostDTO = new PostDTO();
        expectedPostDTO.setId(1L);
        expectedPostDTO.setTitle("Test Post Title");
        expectedPostDTO.setDescription("This is a description.");
        expectedPostDTO.setContent("This is the content of the post.");
        expectedPostDTO.setPostType(PostType.PUBLIC);
        expectedPostDTO.setComments(new HashSet<>()); // Assuming no comments in this case

        // Mock the mapping
        Mockito.when(modelMapper.map(post, PostDTO.class)).thenReturn(expectedPostDTO);

        // Call the method under test
        PostDTO actualPostDTO = postMapperImpl.mapToDTO(post);

        // Verify the results
        assertNotNull(actualPostDTO);
        assertEquals(expectedPostDTO.getId(), actualPostDTO.getId());
        assertEquals(expectedPostDTO.getTitle(), actualPostDTO.getTitle());
        assertEquals(expectedPostDTO.getDescription(), actualPostDTO.getDescription());
        assertEquals(expectedPostDTO.getContent(), actualPostDTO.getContent());
        assertEquals(expectedPostDTO.getPostType(), actualPostDTO.getPostType());
    }

    @Test
    void mapToEntity() {
        // Prepare sample PostDTO
        PostDTO postDTO = new PostDTO();
        postDTO.setId(1L);
        postDTO.setTitle("Test Post Title");
        postDTO.setDescription("This is a description.");
        postDTO.setContent("This is the content of the post.");
        postDTO.setPostType(PostType.PUBLIC);

        // Create the expected Post entity
        Post expectedPost = new Post();
        expectedPost.setId(1L);
        expectedPost.setTitle("Test Post Title");
        expectedPost.setDescription("This is a description.");
        expectedPost.setContent("This is the content of the post.");
        expectedPost.setPostType(PostType.PUBLIC);

        // Mock the mapping
        Mockito.when(modelMapper.map(postDTO, Post.class)).thenReturn(expectedPost);

        // Call the method under test
        Post actualPost = postMapperImpl.mapToEntity(postDTO);

        // Verify the results
        assertNotNull(actualPost);
        assertEquals(expectedPost.getId(), actualPost.getId());
        assertEquals(expectedPost.getTitle(), actualPost.getTitle());
        assertEquals(expectedPost.getDescription(), actualPost.getDescription());
        assertEquals(expectedPost.getContent(), actualPost.getContent());
        assertEquals(expectedPost.getPostType(), actualPost.getPostType());
    }

    @Test
    void mapPostToUserPostDTO() {
        // Prepare sample Post entity
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Post Title");
        post.setDescription("This is a description.");
        post.setContent("This is the content of the post.");
        post.setPostType(PostType.MEMBER_ONLY);

        // Create the expected UserPostDTO
        UserPostDTO expectedUserPostDTO = new UserPostDTO();
        expectedUserPostDTO.setId(1L);
        expectedUserPostDTO.setTitle("Test Post Title");
        expectedUserPostDTO.setDescription("This is a description.");
        expectedUserPostDTO.setContent("This is the content of the post.");

        // Mock the mapping
        Mockito.when(modelMapper.map(post, UserPostDTO.class)).thenReturn(expectedUserPostDTO);

        // Call the method under test
        UserPostDTO actualUserPostDTO = postMapperImpl.mapPostToUserPostDTO(post);

        // Verify the results
        assertNotNull(actualUserPostDTO);
        assertEquals(expectedUserPostDTO.getId(), actualUserPostDTO.getId());
        assertEquals(expectedUserPostDTO.getTitle(), actualUserPostDTO.getTitle());
        assertEquals(expectedUserPostDTO.getDescription(), actualUserPostDTO.getDescription());
        assertEquals(expectedUserPostDTO.getContent(), actualUserPostDTO.getContent());
    }
}