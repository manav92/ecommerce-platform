package com.eshop.catalog.product.api;

import com.eshop.catalog.product.application.ProductVariantService;
import com.eshop.catalog.product.dto.CreateVariantRequest;
import com.eshop.catalog.product.dto.VariantResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/products/{productId}/variants")
public class AdminVariantController {
    private final ProductVariantService variantService;

    public AdminVariantController(ProductVariantService variantService) {
        this.variantService = variantService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VariantResponse create(@PathVariable UUID productId,
                                  @Valid @RequestBody CreateVariantRequest request) {
        return variantService.create(productId, request);
    }
}
