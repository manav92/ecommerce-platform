package com.eshop.catalog.product.dto;

import com.eshop.catalog.product.domain.ProductStatus;
import java.time.Instant;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String productCode,
        String name,
        String brand,
        String category,
        String description,
        ProductStatus status,
        Instant createdAt,
        Instant updatedAt
) {}
