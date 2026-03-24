package com.eshop.importworker.batch.model;

import java.math.BigDecimal;

public record ProcessedProductRow(
        String productCode,
        String name,
        String brand,
        String category,
        String description,
        String sku,
        String size,
        String design,
        BigDecimal priceAmount,
        String currency
) {}
