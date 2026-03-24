package com.eshop.catalog.product.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateProductRequest(
        @NotBlank String productCode,
        @NotBlank String name,
        String brand,
        String category,
        String description
) {}
