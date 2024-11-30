package com.BlogifyHub.model.mapper.impl;

import com.BlogifyHub.model.DTO.CategoryDTO;
import com.BlogifyHub.model.entity.Category;
import com.BlogifyHub.model.entity.enums.CategoryType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperImplTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryMapperImpl categoryMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void categoryToCategoryDTO() {
        // Arrange
        Category category = new Category();
        category.setId(1L);
        category.setCategoryType(CategoryType.TECHNOLOGY);
        category.setDescription("Technology related Posts");
        category.setPosts(new ArrayList<>()); // Simulating empty posts for simplicity

        CategoryDTO expectedCategoryDTO = new CategoryDTO();
        expectedCategoryDTO.setId(1L);
        expectedCategoryDTO.setCategoryType(CategoryType.TECHNOLOGY);
        expectedCategoryDTO.setDescription("Technology related Posts");

        Mockito.when(modelMapper.map(category, CategoryDTO.class)).thenReturn(expectedCategoryDTO);

        // Act
        CategoryDTO result = categoryMapper.categoryToCategoryDTO(category);

        // Assert
        assertNotNull(result);
        assertEquals(expectedCategoryDTO.getId(), result.getId());
        assertEquals(expectedCategoryDTO.getCategoryType(), result.getCategoryType());
        assertEquals(expectedCategoryDTO.getDescription(), result.getDescription());
        Mockito.verify(modelMapper, Mockito.times(1)).map(category,CategoryDTO.class);
    }

    @Test
    void categoryDtoToCategory() {
        // Arrange
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setCategoryType(CategoryType.TECHNOLOGY);
        categoryDTO.setDescription("Technology related posts");

        Category expectedCategory = new Category();
        expectedCategory.setId(1L);
        expectedCategory.setCategoryType(CategoryType.TECHNOLOGY);
        expectedCategory.setDescription("Technology related posts");

        /* The when(...) function defines the condition under which the mock will return a specific value.
         It essentially says: "When this method is called with these inputs, respond with this value."*/
        Mockito.when(modelMapper.map(categoryDTO, Category.class)).thenReturn(expectedCategory);

        // Act
        Category result = categoryMapper.categoryDtoToCategory(categoryDTO);

        // Assert
        assertNotNull(result);
        assertEquals(expectedCategory.getId(), result.getId());
        assertEquals(expectedCategory.getCategoryType(), result.getCategoryType());
        assertEquals(expectedCategory.getDescription(), result.getDescription());
        Mockito.verify(modelMapper, Mockito.times(1)).map(categoryDTO, Category.class);
    }
}