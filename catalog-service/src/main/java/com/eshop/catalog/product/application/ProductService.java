package com.eshop.catalog.product.application;

import com.eshop.catalog.product.domain.Product;
import com.eshop.catalog.product.dto.CreateProductRequest;
import com.eshop.catalog.product.dto.ProductResponse;
import com.eshop.catalog.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        Product product = new Product();
        product.setProductCode(request.productCode());
        product.setName(request.name());
        product.setBrand(request.brand());
        product.setCategory(request.category());
        product.setDescription(request.description());

        Product saved = productRepository.save(product);
        return new ProductResponse(
                saved.getId(),
                saved.getProductCode(),
                saved.getName(),
                saved.getBrand(),
                saved.getCategory(),
                saved.getDescription(),
                saved.getStatus(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }
}
