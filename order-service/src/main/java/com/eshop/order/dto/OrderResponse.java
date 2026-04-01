package com.eshop.order.dto;

import com.eshop.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String customerId,
        OrderStatus status,
        BigDecimal grandTotal,
        String currency,
        String failureReason,
        List<OrderItemResponse> items,
        Instant createdAt,
        Instant updatedAt
) {}
