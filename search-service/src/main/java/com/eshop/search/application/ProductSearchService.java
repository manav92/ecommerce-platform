package com.eshop.search.application;

import com.eshop.search.document.ProductDocument;
import com.eshop.search.dto.ProductSearchResponse;
import com.eshop.search.repository.ProductSearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSearchService {
    private final ProductSearchRepository repository;

    public ProductSearchService(ProductSearchRepository repository) {
        this.repository = repository;
    }

    public List<ProductSearchResponse> search(String q) {
        return repository.findByNameContainingOrBrandContainingOrCategoryContaining(q, q, q)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ProductSearchResponse toResponse(ProductDocument doc) {
        return new ProductSearchResponse(
                doc.getId(),
                doc.getProductCode(),
                doc.getSku(),
                doc.getName(),
                doc.getBrand(),
                doc.getCategory(),
                doc.getDescription(),
                doc.getSize(),
                doc.getDesign(),
                doc.getPriceAmount(),
                doc.getCurrency()
        );
    }
}
