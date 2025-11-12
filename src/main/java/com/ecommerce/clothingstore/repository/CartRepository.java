package com.ecommerce.clothingstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.clothingstore.entity.Cart;
import com.ecommerce.clothingstore.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long>{

	Optional<Cart> findByUser(User user);
}
