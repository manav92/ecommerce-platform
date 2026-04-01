package com.eshop.cart.dto;

import com.eshop.cart.domain.CartStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record CartResponse(
        String customerId,
        CartStatus status,
        List<CartItemResponse> items,
        Integer totalItems,
        BigDecimal grandTotal,
        String currency,
        Instant createdAt,
        Instant updatedAt
) {}
