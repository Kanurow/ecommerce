package com.rowland.engineering.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ProductRequest {

    @NotBlank
    @Size(max = 40)
    private String productName;

    @NotNull
    @Positive
    private Integer price;

    @NotNull
    @Positive
    private Integer quantity;
}
