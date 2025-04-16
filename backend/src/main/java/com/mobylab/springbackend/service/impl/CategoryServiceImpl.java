package com.mobylab.springbackend.service.impl;

import com.mobylab.springbackend.dto.CategoryDto;
import com.mobylab.springbackend.dto.PageResponseDto;
import com.mobylab.springbackend.dto.mapper.EntityMapper;
import com.mobylab.springbackend.entity.Category;
import com.mobylab.springbackend.exception.ResourceAlreadyExistsException;
import com.mobylab.springbackend.exception.ResourceNotFoundException;
import com.mobylab.springbackend.repository.CategoryRepository;
import com.mobylab.springbackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EntityMapper mapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, EntityMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new ResourceAlreadyExistsException("Category already exists with name: " + categoryDto.getName());
        }
        
        Category category = mapper.toEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        
        return mapper.toDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        
        return mapper.toDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
        
        return mapper.toDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll(Sort.by("name"));
        
        return categories.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<CategoryDto> getAllCategoriesPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        
        List<CategoryDto> categoryDtos = categoryPage.getContent().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        
        return new PageResponseDto<>(categoryDtos, categoryPage);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(UUID id, CategoryDto categoryDto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        
        // Check if name is being changed and if new name already exists
        if (!existingCategory.getName().equals(categoryDto.getName()) && 
            categoryRepository.existsByName(categoryDto.getName())) {
            throw new ResourceAlreadyExistsException("Category already exists with name: " + categoryDto.getName());
        }
        
        existingCategory.setName(categoryDto.getName());
        existingCategory.setDescription(categoryDto.getDescription());
        
        Category updatedCategory = categoryRepository.save(existingCategory);
        
        return mapper.toDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        
        categoryRepository.delete(category);
    }
} 