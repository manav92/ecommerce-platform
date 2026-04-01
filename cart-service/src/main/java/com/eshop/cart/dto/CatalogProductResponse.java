package com.eshop.cart.dto;

import java.util.List;
import java.util.UUID;

public record CatalogProductResponse(
        UUID id,
        String productCode,
        String name,
        String brand,
        String category,
        String description,
        String status,
        List<CatalogVariantResponse> variants
) {}
