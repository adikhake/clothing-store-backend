package com.ecommerce.clothingstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private UserDTO user;
    private List<OrderItemDTO> items;
    private double totalAmount;
    private String status;
    private String paymentStatus;
    private LocalDateTime orderDate;
}
