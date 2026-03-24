package com.eshop.search.dto;

import java.math.BigDecimal;

public record ProductSearchResponse(
        String id,
        String productCode,
        String sku,
        String name,
        String brand,
        String category,
        String description,
        String size,
        String design,
        BigDecimal priceAmount,
        String currency
) {}
