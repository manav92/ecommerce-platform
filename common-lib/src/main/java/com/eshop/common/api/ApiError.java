package com.eshop.common.api;

import java.time.Instant;
import java.util.List;

public record ApiError(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        String errorCode,
        String correlationId,
        Instant timestamp,
        List<FieldErrorItem> fieldErrors
) {
    public record FieldErrorItem(String field, String message) {}
}
