package com.eshop.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpsertInventoryRequest(
        @NotBlank String productCode,
        @NotBlank String sku,
        @Min(0) int totalQuantity
) {}
