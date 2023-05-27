package com.rowland.engineering.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "cart_checkout")
public class CartCheckout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderAddress;

    private double price;

    private int quantity;

    private Long userId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_checkout_id")
    private List<CartItem> cart;

    // Constructors, getters, and setters

    @Data
    @Entity
    @Table(name = "cart_item")
    public static class CartItem {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String productName;

        private double price;

        private int quantity;

        private double subtotal;

        // Constructors, getters, and setters
    }
}
