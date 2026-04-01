package com.eshop.order.dto;

import jakarta.validation.constraints.NotBlank;

public record PlaceOrderRequest(
        @NotBlank String customerId,
        @NotBlank String idempotencyKey
) {}
