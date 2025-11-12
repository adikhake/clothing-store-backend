package com.ecommerce.clothingstore.mapper;

import com.ecommerce.clothingstore.dto.*;
import com.ecommerce.clothingstore.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityMapper {

    public UserDTO toUserDTO(User user) {
        return new UserDTO(user.getUserId(), user.getName(), user.getEmail());
    }

    public CartDTO toCartDTO(Cart cart) {
        if (cart == null) return null;

        List<CartItemDTO> items = cart.getCartItems().stream()
                .map(i -> new CartItemDTO(
                        i.getProduct().getProductId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getProduct().getPrice(),
                        i.getProduct().getPrice() * i.getQuantity()
                )).collect(Collectors.toList());

        double total = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();

        return new CartDTO(cart.getCartId(), cart.getUser().getUserId(), items, total);
    }
    
    public ProductDTO toProductDTO(Product product) {
        if (product == null) return null;

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getProductId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());

        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getName());
        }

        return dto;
    }
    
    public OrderDTO toOrderDTO(Order order) {
        List<OrderItemDTO> items = order.getOrderItems().stream()
                .map(i -> new OrderItemDTO(
                        i.getProduct().getProductId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getPrice()
                )).collect(Collectors.toList());

        double total = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();

        return new OrderDTO(
                order.getOrderId(),
                toUserDTO(order.getUser()),
                items,
                total,
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getOrderDate()
        );
    }
    
    public CategoryDTO toCategoryDTO(Category category) {
        if (category == null) return null;

        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getCategoryId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }

    public Category toCategory(CategoryDTO dto) {
        if (dto == null) return null;

        Category category = new Category();
        category.setCategoryId(dto.getId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }


}
