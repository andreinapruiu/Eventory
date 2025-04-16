package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.dto.CategoryDto;
import com.mobylab.springbackend.dto.PageResponseDto;
import com.mobylab.springbackend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Management", description = "APIs for category management")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new category", description = "Create a new event category")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a category by ID", description = "Retrieve a category by its ID")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable UUID id) {
        CategoryDto category = categoryService.getCategory(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get a category by name", description = "Retrieve a category by its name")
    public ResponseEntity<CategoryDto> getCategoryByName(@PathVariable String name) {
        CategoryDto category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(category);
    }

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieve all categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/paged")
    @Operation(summary = "Get all categories with pagination", description = "Retrieve all categories with pagination")
    public ResponseEntity<PageResponseDto<CategoryDto>> getAllCategoriesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponseDto<CategoryDto> categories = categoryService.getAllCategoriesPaged(page, size);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a category", description = "Update an existing category")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a category", description = "Delete a category by its ID")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
} 