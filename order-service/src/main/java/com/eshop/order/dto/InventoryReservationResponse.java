package com.eshop.order.dto;

import java.time.Instant;
import java.util.UUID;

public record InventoryReservationResponse(
        UUID id,
        String sku,
        String referenceType,
        String referenceId,
        Integer quantity,
        String status,
        Instant createdAt,
        Instant expiresAt,
        Instant updatedAt
) {}
