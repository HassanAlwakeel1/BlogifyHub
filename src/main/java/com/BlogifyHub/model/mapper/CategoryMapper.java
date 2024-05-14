package com.BlogifyHub.model.mapper;

import com.BlogifyHub.model.DTO.CategoryDTO;
import com.BlogifyHub.model.entity.Category;

public interface CategoryMapper {
    public CategoryDTO categoryToCategoryDTO(Category category);
    public Category categoryDtoToCategory(CategoryDTO categoryDTO);
}
