package com.eshop.cart.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CatalogVariantResponse(
        UUID id,
        String sku,
        String size,
        String design,
        BigDecimal priceAmount,
        String currency,
        String status
) {}
