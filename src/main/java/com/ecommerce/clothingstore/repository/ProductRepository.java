package com.ecommerce.clothingstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.clothingstore.entity.Category;
import com.ecommerce.clothingstore.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findByCategory_CategoryId(Long categoryId);

	List<Product> findByCategory(Category category);

	boolean existsByNameIgnoreCase(String name);

	void deleteByName(String name);

	Optional<Product> findByNameIgnoreCase(String name);

}
