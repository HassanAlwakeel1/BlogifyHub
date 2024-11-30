package com.BlogifyHub.model.mapper.impl;

import com.BlogifyHub.model.DTO.CommentDTO;
import com.BlogifyHub.model.DTO.ProfileResponseDTO;
import com.BlogifyHub.model.entity.Category;
import com.BlogifyHub.model.entity.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperImplTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentMapperImpl commentMapper;

    @BeforeEach()
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void mapToEntity() {
        // Arrange
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setBody("This is test comment.");

        Comment expectedComment = new Comment();
        expectedComment.setId(1L);
        expectedComment.setBody("This is test comment.");

        Mockito.when(modelMapper.map(commentDTO,Comment.class)).thenReturn(expectedComment);

        // Act
        Comment result = commentMapper.mapToEntity(commentDTO);

        // Assert
        assertNotNull(result);
        assertEquals(expectedComment.getId(), result.getId());
        assertEquals(expectedComment.getBody(), result.getBody());
        Mockito.verify(modelMapper, Mockito.times(1)).map(commentDTO, Comment.class);

    }

    @Test
    void mapToDTO() {
        // Arrange
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setBody("This is a test comment.");

        CommentDTO expectedCommmentDTO = new CommentDTO();
        expectedCommmentDTO.setId(1L);
        expectedCommmentDTO.setBody("This is a test comment.");

        Mockito.when(modelMapper.map(comment, CommentDTO.class)).thenReturn(expectedCommmentDTO);

        // Act
        CommentDTO result = commentMapper.mapToDTO(comment);

        // Assert
        assertNotNull(result);
        assertEquals(expectedCommmentDTO.getId(), result.getId());
        assertEquals(expectedCommmentDTO.getBody(), result.getBody());
        Mockito.verify(modelMapper, Mockito.times(1)).map(comment, CommentDTO.class);

    }
}