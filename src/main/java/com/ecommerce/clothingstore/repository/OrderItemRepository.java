package com.ecommerce.clothingstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.clothingstore.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
