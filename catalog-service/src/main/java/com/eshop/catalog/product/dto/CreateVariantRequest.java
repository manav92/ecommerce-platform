package com.eshop.catalog.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateVariantRequest(
        @NotBlank String sku,
        @NotBlank String size,
        @NotBlank String design,
        @NotNull @DecimalMin("0.0") BigDecimal priceAmount,
        @NotBlank String currency
) {}
