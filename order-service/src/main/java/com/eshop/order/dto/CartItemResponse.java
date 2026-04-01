package com.eshop.order.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponse(
        UUID id,
        String productCode,
        String sku,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal,
        String currency
) {}
