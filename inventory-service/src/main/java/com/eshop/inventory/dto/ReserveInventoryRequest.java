package com.eshop.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ReserveInventoryRequest(
        @NotBlank String sku,
        @NotBlank String referenceType,
        @NotBlank String referenceId,
        @Min(1) int quantity,
        @Min(1) int reservationTtlMinutes
) {}
