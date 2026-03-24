package com.eshop.catalog.product.application;

import com.eshop.catalog.product.domain.Product;
import com.eshop.catalog.product.domain.ProductVariant;
import com.eshop.catalog.product.dto.CreateVariantRequest;
import com.eshop.catalog.product.dto.VariantResponse;
import com.eshop.catalog.product.repository.ProductRepository;
import com.eshop.catalog.product.repository.ProductVariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class ProductVariantService {
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;

    public ProductVariantService(ProductRepository productRepository,
                                 ProductVariantRepository variantRepository) {
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
    }

    @Transactional
    public VariantResponse create(UUID productId, CreateVariantRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setSku(request.sku());
        variant.setSize(request.size());
        variant.setDesign(request.design());
        variant.setPriceAmount(request.priceAmount());
        variant.setCurrency(request.currency());

        ProductVariant saved = variantRepository.save(variant);

        return new VariantResponse(
                saved.getId(),
                product.getId(),
                saved.getSku(),
                saved.getSize(),
                saved.getDesign(),
                saved.getPriceAmount(),
                saved.getCurrency(),
                saved.getStatus(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }
}
