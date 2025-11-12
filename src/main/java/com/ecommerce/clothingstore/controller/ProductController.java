package com.ecommerce.clothingstore.controller;

import com.ecommerce.clothingstore.dto.ProductDTO;
import com.ecommerce.clothingstore.entity.Product;
import com.ecommerce.clothingstore.mapper.EntityMapper;
import com.ecommerce.clothingstore.payload.ApiResponse;
import com.ecommerce.clothingstore.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final EntityMapper entityMapper;

    public ProductController(ProductService productService, EntityMapper entityMapper) {
        this.productService = productService;
        this.entityMapper = entityMapper;
    }

    // ➕ Add new product under a category
    @PostMapping("/category/{categoryName}")
    public ResponseEntity<ApiResponse<ProductDTO>> addProduct(
            @RequestBody Product product,
            @PathVariable String categoryName) {

        Product saved = productService.addProduct(product, categoryName);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product added successfully", entityMapper.toProductDTO(saved))
        );
    }

    // 📋 Get all products
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts()
                .stream()
                .map(entityMapper::toProductDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Products fetched successfully", products)
        );
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductByName(@PathVariable String name) {
        Product product = productService.getProductByName(name);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product fetched successfully", entityMapper.toProductDTO(product))
        );
    }

    // 🧩 Get products by category
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByCategory(@PathVariable String categoryName) {
        List<ProductDTO> products = productService.getProductsByCategory(categoryName)
                .stream()
                .map(entityMapper::toProductDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Products fetched successfully by category", products)
        );
    }

    // 🗑️ Delete product
    @DeleteMapping("/{name}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String name) {
        productService.deleteProduct(name);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product deleted successfully", null)
        );
    }
}
