package com.mobylab.springbackend.service;

import com.mobylab.springbackend.dto.CategoryDto;
import com.mobylab.springbackend.dto.PageResponseDto;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto getCategory(UUID id);
    CategoryDto getCategoryByName(String name);
    List<CategoryDto> getAllCategories();
    PageResponseDto<CategoryDto> getAllCategoriesPaged(int page, int size);
    CategoryDto updateCategory(UUID id, CategoryDto categoryDto);
    void deleteCategory(UUID id);
} 