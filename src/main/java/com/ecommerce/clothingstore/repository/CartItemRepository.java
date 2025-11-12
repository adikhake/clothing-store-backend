package com.ecommerce.clothingstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.clothingstore.entity.Cart;
import com.ecommerce.clothingstore.entity.CartItem;
import com.ecommerce.clothingstore.entity.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    void deleteByCartAndProduct(Cart cart, Product product);

}
