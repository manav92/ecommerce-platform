package com.eshop.order.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderPlacedEvent(
        UUID orderId,
        String customerId,
        BigDecimal grandTotal,
        String currency,
        String status
) {}
