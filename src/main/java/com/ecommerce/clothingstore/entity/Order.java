package com.ecommerce.clothingstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private Double totalAmount;
    private String paymentStatus;
    private String orderStatus;
    private LocalDateTime orderDate = LocalDateTime.now();

    // Constructors
    public Order() {}

    public Order(User user, Double totalAmount, String paymentStatus, String orderStatus) {
        this.user = user;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.orderStatus = orderStatus;
    }

}
