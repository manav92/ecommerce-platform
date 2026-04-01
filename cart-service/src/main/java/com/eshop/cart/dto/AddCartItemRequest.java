package com.eshop.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AddCartItemRequest(
        @NotBlank String customerId,
        @NotBlank String productCode,
        @NotBlank String sku,
        @NotBlank String productName,
        @Min(1) int quantity
) {}
