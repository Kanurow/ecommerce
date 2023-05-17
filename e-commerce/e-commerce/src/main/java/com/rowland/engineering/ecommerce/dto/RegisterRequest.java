package com.rowland.engineering.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {

    private String name;

    private String username;

    private String mobile;
    private String promoCode;
    private String email;

    private String password;
}
