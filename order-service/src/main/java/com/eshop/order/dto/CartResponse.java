package com.eshop.order.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record CartResponse(
        String customerId,
        String status,
        List<CartItemResponse> items,
        Integer totalItems,
        BigDecimal grandTotal,
        String currency,
        Instant createdAt,
        Instant updatedAt
) {}
