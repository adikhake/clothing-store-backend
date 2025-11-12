package com.ecommerce.clothingstore.controller;

import com.ecommerce.clothingstore.dto.OrderDTO;
import com.ecommerce.clothingstore.entity.Order;
import com.ecommerce.clothingstore.mapper.EntityMapper;
import com.ecommerce.clothingstore.payload.ApiResponse;
import com.ecommerce.clothingstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final EntityMapper entityMapper;

    // 🧾 Place new order for user
    @PostMapping("/place/{userId}")
    public ResponseEntity<ApiResponse<OrderDTO>> placeOrder(@PathVariable Long userId) {
        Order order = orderService.placeOrder(userId);
        OrderDTO dto = entityMapper.toOrderDTO(order);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Order placed successfully", dto)
        );
    }

    // 📋 Get all orders for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        List<OrderDTO> dtos = orders.stream()
                .map(entityMapper::toOrderDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Orders fetched successfully", dtos)
        );
    }

    // 🔍 Get single order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null)
            return ResponseEntity.ok(new ApiResponse<>(false, "Order not found", null));
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Order fetched successfully", entityMapper.toOrderDTO(order))
        );
    }

    // 🧾 Update order status (Admin use)
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
