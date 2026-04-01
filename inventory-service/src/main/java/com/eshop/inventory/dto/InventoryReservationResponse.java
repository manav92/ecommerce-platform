package com.eshop.inventory.dto;

import com.eshop.inventory.domain.InventoryReservationStatus;

import java.time.Instant;
import java.util.UUID;

public record InventoryReservationResponse(
        UUID id,
        String sku,
        String referenceType,
        String referenceId,
        Integer quantity,
        InventoryReservationStatus status,
        Instant createdAt,
        Instant expiresAt,
        Instant updatedAt
) {}
