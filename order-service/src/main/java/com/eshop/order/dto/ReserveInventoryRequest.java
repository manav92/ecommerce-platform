package com.eshop.order.dto;

public record ReserveInventoryRequest(
        String sku,
        String referenceType,
        String referenceId,
        int quantity,
        int reservationTtlMinutes
) {}
