package com.ecommerce.clothingstore.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch =FetchType.LAZY )
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
    private Double price;
    
    private LocalDateTime addedAt = LocalDateTime.now();

    // Constructors
    public CartItem() {}

    public CartItem(Cart cart, Product product, Integer quantity, Double price) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.addedAt = LocalDateTime.now();
    }
 
    public double getSubTotal() {
        return this.quantity * this.price;
    }

}
