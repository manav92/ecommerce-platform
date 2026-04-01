package com.eshop.inventory.dto;

import java.time.Instant;
import java.util.UUID;

public record InventoryItemResponse(
        UUID id,
        String productCode,
        String sku,
        Integer totalQuantity,
        Integer reservedQuantity,
        Integer availableQuantity,
        Instant createdAt,
        Instant updatedAt
) {}
