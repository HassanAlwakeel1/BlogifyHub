package com.BlogifyHub.model.mapper.impl;

import com.BlogifyHub.model.DTO.CategoryDTO;
import com.BlogifyHub.model.entity.Category;
import com.BlogifyHub.model.mapper.CategoryMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapperImpl implements CategoryMapper {

    private ModelMapper mapper;

    public CategoryMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CategoryDTO categoryToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = mapper.map(category,CategoryDTO.class);
        return categoryDTO;
    }

    @Override
    public Category categoryDtoToCategory(CategoryDTO categoryDTO) {
        Category category = mapper.map(categoryDTO,Category.class);
        return category;
    }
}
