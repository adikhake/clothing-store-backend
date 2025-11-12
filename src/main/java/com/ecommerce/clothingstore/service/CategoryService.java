package com.ecommerce.clothingstore.service;

import com.ecommerce.clothingstore.dto.CategoryDTO;
import com.ecommerce.clothingstore.entity.Category;
import com.ecommerce.clothingstore.exception.ResourceNotFoundException;
import com.ecommerce.clothingstore.mapper.EntityMapper;
import com.ecommerce.clothingstore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final EntityMapper entityMapper;

    // ➕ Add new category
    public CategoryDTO addCategory(CategoryDTO dto) {
        Category category = entityMapper.toCategory(dto);
        category = categoryRepository.save(category);
        return entityMapper.toCategoryDTO(category);
    }

    // 📋 Get all categories
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(entityMapper::toCategoryDTO)
                .collect(Collectors.toList());
    }

    // 🔍 Get category by name
    public CategoryDTO getCategoryByName(String name) {
        Category category = categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
        return entityMapper.toCategoryDTO(category);
    }

    // ✏️ Update category
    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());

        Category updated = categoryRepository.save(existing);
        return entityMapper.toCategoryDTO(updated);
    }

    // ❌ Delete category
    public void deleteCategory(String name) {
        Category category = categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name " + name));
        categoryRepository.delete(category);
    }
}
