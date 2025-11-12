package com.ecommerce.clothingstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
	private Long cartId;
    private Long userId;
//    private UserDTO user;
    private List<CartItemDTO> items;
    private double totalPrice;
}
