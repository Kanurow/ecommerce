package com.rowland.engineering.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartCheckoutRequest {
    private String orderAddress;
    private double total;
    private int quantity;
    private Long userId;
    private List<CartItem> cart;

    // Constructors, getters, and setters

    // Inner class representing a cart item
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartItem {
        private String productName;
        private double price;
        private int quantity;
        private double subtotal;

        // Constructors, getters, and setters
    }
}

