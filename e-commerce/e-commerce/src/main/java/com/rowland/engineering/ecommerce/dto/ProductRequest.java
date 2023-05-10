package com.rowland.engineering.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

@Data
@AllArgsConstructor
public class ProductRequest {

    @NaturalId
    private String productName;

    @NotNull
    private Integer price;

    private Integer quantity;
}
