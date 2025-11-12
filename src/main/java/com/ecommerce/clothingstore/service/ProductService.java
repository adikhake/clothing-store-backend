package com.ecommerce.clothingstore.service;

import com.ecommerce.clothingstore.entity.Category;
import com.ecommerce.clothingstore.entity.Product;
import com.ecommerce.clothingstore.exception.ResourceNotFoundException;
import com.ecommerce.clothingstore.repository.CategoryRepository;
import com.ecommerce.clothingstore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // ➕ Add new product
    public Product addProduct(Product product, String categoryName) {
        Category category = categoryRepository.findByNameIgnoreCase(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        product.setCategory(category);
        return productRepository.save(product);
    }

    // 📋 Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 🔍 Get product by ID
    public Product getProductByName(String name) {
        return productRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    // 🧩 Get products by category
    public List<Product> getProductsByCategory(String categoryName) {
        Category category = categoryRepository.findByNameIgnoreCase(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return productRepository.findByCategory(category);
    }

    // 🗑️ Delete product
    public void deleteProduct(String name) {
        if (!productRepository.existsByNameIgnoreCase(name)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteByName(name);
    }

}
