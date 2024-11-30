package com.BlogifyHub.model.mapper.impl;

import com.BlogifyHub.model.DTO.PostDTO;
import com.BlogifyHub.model.DTO.PostListDTO;
import com.BlogifyHub.model.entity.Post;
import com.BlogifyHub.model.entity.PostList;
import com.BlogifyHub.model.entity.enums.ListStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PostListMapperImplTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PostListMapperImpl postListMapperImpl;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void mapToDTO() {
        // Prepare sample PostList entity
        PostList postList = new PostList();
        postList.setId(1L);
        postList.setName("Test List");
        postList.setDescription("This is a test list description.");
        postList.setListStatus(ListStatus.PUBLIC);  // Assuming you have this status in ListStatus
        postList.setCreatedAt(new java.util.Date());

        // Create a set of PostDTOs to simulate posts in the list
        Set<Post> posts = new HashSet<>();
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Post");
        post.setDescription("Post description");
        posts.add(post);

        // Set the posts in the PostList
        postList.setPosts(posts);

        // Create expected PostListDTO
        PostListDTO expectedPostListDTO = new PostListDTO();
        expectedPostListDTO.setId(1L);
        expectedPostListDTO.setName("Test List");
        expectedPostListDTO.setDescription("This is a test list description.");
        expectedPostListDTO.setListStatus(ListStatus.PUBLIC);
        expectedPostListDTO.setCreatedAt(postList.getCreatedAt());
        expectedPostListDTO.setPostDTO(new HashSet<>()); // Assuming no posts are converted yet.

        // Mock the mapping
        Mockito.when(modelMapper.map(postList, PostListDTO.class)).thenReturn(expectedPostListDTO);

        // Call the method under test
        PostListDTO actualPostListDTO = postListMapperImpl.mapToDTO(postList);

        // Verify the results
        assertNotNull(actualPostListDTO);
        assertEquals(expectedPostListDTO.getId(), actualPostListDTO.getId());
        assertEquals(expectedPostListDTO.getName(), actualPostListDTO.getName());
        assertEquals(expectedPostListDTO.getDescription(), actualPostListDTO.getDescription());
        assertEquals(expectedPostListDTO.getListStatus(), actualPostListDTO.getListStatus());
        assertEquals(expectedPostListDTO.getCreatedAt(), actualPostListDTO.getCreatedAt());

    }

    @Test
    void mapToEntity() {
        // Prepare sample PostListDTO
        PostListDTO postListDTO = new PostListDTO();
        postListDTO.setId(1L);
        postListDTO.setName("Test List");
        postListDTO.setDescription("This is a test list description.");
        postListDTO.setListStatus(ListStatus.PUBLIC);  // Assuming you have this status in ListStatus
        postListDTO.setCreatedAt(new java.util.Date());

        // Create a set of PostDTOs to simulate posts in the list
        Set<PostDTO> postDTOs = new HashSet<>();
        PostDTO postDTO = new PostDTO();
        postDTO.setId(1L);
        postDTO.setTitle("Test Post");
        postDTO.setDescription("Post description");
        postDTOs.add(postDTO);

        // Set the posts in the PostListDTO
        postListDTO.setPostDTO(postDTOs);

        // Create expected PostList entity
        PostList expectedPostList = new PostList();
        expectedPostList.setId(1L);
        expectedPostList.setName("Test List");
        expectedPostList.setDescription("This is a test list description.");
        expectedPostList.setListStatus(ListStatus.PUBLIC);
        expectedPostList.setCreatedAt(postListDTO.getCreatedAt());

        // Mock the mapping
        Mockito.when(modelMapper.map(postListDTO, PostList.class)).thenReturn(expectedPostList);

        // Call the method under test
        PostList actualPostList = postListMapperImpl.mapToEntity(postListDTO);

        // Verify the results
        assertNotNull(actualPostList);
        assertEquals(expectedPostList.getId(), actualPostList.getId());
        assertEquals(expectedPostList.getName(), actualPostList.getName());
        assertEquals(expectedPostList.getDescription(), actualPostList.getDescription());
        assertEquals(expectedPostList.getListStatus(), actualPostList.getListStatus());
        assertEquals(expectedPostList.getCreatedAt(), actualPostList.getCreatedAt());

    }
}