package com.eshop.catalog.product.api;

import com.eshop.catalog.product.application.ProductService;
import com.eshop.catalog.product.dto.CreateProductRequest;
import com.eshop.catalog.product.application.ProductVariantService;
import com.eshop.catalog.product.dto.ProductResponse;
import com.eshop.catalog.product.dto.VariantResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService service;
    private final ProductVariantService productVariantService;

    public ProductController(ProductService service, ProductVariantService productVariantService) {
        this.service = service;
        this.productVariantService = productVariantService;
    }

   @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody CreateProductRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<ProductResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{productCode}")
    public ProductResponse getByProductCode(@PathVariable String productCode) {
        return service.getByProductCode(productCode);
    }

    @GetMapping("/sku/{sku}")
    public VariantResponse getBySku(@PathVariable String sku) {
        return service.getBySku(sku);
    }
}
