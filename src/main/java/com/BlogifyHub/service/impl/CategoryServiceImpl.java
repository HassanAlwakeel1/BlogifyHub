package com.BlogifyHub.service.impl;

import com.BlogifyHub.exception.ResourceNotFoundException;
import com.BlogifyHub.model.DTO.CategoryDTO;
import com.BlogifyHub.model.entity.Category;
import com.BlogifyHub.model.mapper.CategoryMapper;
import com.BlogifyHub.repository.CategoryRepository;
import com.BlogifyHub.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.categoryDtoToCategory(categoryDTO);
        Category savedCategory =  categoryRepository.save(category);
        CategoryDTO categoryDTOResponse = categoryMapper.categoryToCategoryDTO(savedCategory);
        return categoryDTOResponse;
    }

    @Override
    public CategoryDTO getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","id",categoryId));
        CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDTO(category);
        return categoryDTO;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOList =  categories
                .stream()
                .map((category) -> categoryMapper.categoryToCategoryDTO(category))
                .collect(Collectors.toList());
        return categoryDTOList;
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","id",categoryId));

        category.setId(categoryId);
        category.setCategoryType(categoryDTO.getCategoryType());
        category.setDescription(categoryDTO.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        CategoryDTO categoryDTOResponse = categoryMapper.categoryToCategoryDTO(updatedCategory);
        return categoryDTOResponse;
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","id",categoryId));
        categoryRepository.delete(category);
    }
}
