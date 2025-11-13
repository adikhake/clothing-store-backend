package com.ecommerce.clothingstore.controller;

import com.ecommerce.clothingstore.dto.OrderDTO;
import com.ecommerce.clothingstore.entity.Order;
import com.ecommerce.clothingstore.entity.User;
import com.ecommerce.clothingstore.mapper.EntityMapper;
import com.ecommerce.clothingstore.payload.ApiResponse;
import com.ecommerce.clothingstore.repository.UserRepository;
import com.ecommerce.clothingstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final EntityMapper entityMapper;
    private final UserRepository userRepository;
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping("/place")
    public ResponseEntity<ApiResponse<OrderDTO>> placeOrder() {
    	User user = getCurrentUser();
        Order order = orderService.placeOrder(user.getUserId());
        OrderDTO dto = entityMapper.toOrderDTO(order);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Order placed successfully", dto)
        );
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrdersByUser() {
    	User user = getCurrentUser();
        List<Order> orders = orderService.getOrdersByUser(user.getUserId());
        List<OrderDTO> dtos = orders.stream()
                .map(entityMapper::toOrderDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Orders fetched successfully", dtos)
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null)
            return ResponseEntity.ok(new ApiResponse<>(false, "Order not found", null));
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Order fetched successfully", entityMapper.toOrderDTO(order))
        );
    }

    // (Admin use)
    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {

        Order updated = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Order status updated successfully", entityMapper.toOrderDTO(updated))
        );
    }
}
