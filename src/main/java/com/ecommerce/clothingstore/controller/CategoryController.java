package com.ecommerce.clothingstore.controller;

import com.ecommerce.clothingstore.dto.CategoryDTO;
import com.ecommerce.clothingstore.payload.ApiResponse;
import com.ecommerce.clothingstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // ➕ Add new category
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDTO>> addCategory(@RequestBody CategoryDTO dto) {
        CategoryDTO created = categoryService.addCategory(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category added successfully", created));
    }

    // 📋 Get all categories
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(new ApiResponse<>(true, "Categories fetched successfully", categories));
    }

    // 🔍 Get category by name
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryByName(@PathVariable String name) {
        CategoryDTO category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category fetched successfully", category));
    }

    // ✏️ Update category
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDTO dto) {
        CategoryDTO updated = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category updated successfully", updated));
    }

    // ❌ Delete category
    @DeleteMapping("/{name}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable String name) {
        categoryService.deleteCategory(name);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category deleted successfully", null));
    }
}
