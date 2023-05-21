package com.rowland.engineering.ecommerce.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String name;

    private String username;

    private String mobile;

    private String email;
}
