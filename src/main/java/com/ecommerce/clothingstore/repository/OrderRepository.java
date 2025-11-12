package com.ecommerce.clothingstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.clothingstore.entity.Order;
import java.util.List;
import com.ecommerce.clothingstore.entity.User;


public interface OrderRepository extends JpaRepository<Order, Long> {
   
	List<Order> findByUser(User user);
}
