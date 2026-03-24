package com.eshop.search.api;

import com.eshop.search.application.ProductSearchService;
import com.eshop.search.dto.ProductSearchResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search/products")
public class ProductSearchController {
    private final ProductSearchService service;

    public ProductSearchController(ProductSearchService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductSearchResponse> search(@RequestParam("q") String query) {
        return service.search(query);
    }
}
