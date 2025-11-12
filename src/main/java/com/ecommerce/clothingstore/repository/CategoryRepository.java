package com.ecommerce.clothingstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.clothingstore.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

	Optional<Category> findByNameIgnoreCase(String name);

}
