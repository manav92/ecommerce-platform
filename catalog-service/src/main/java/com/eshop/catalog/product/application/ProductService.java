package com.eshop.catalog.product.application;

import com.eshop.catalog.product.domain.Product;
import com.eshop.catalog.product.domain.ProductVariant;
import com.eshop.catalog.product.dto.CreateProductRequest;
import com.eshop.catalog.product.dto.ProductResponse;
import com.eshop.catalog.product.dto.VariantResponse;
import com.eshop.catalog.product.repository.ProductRepository;
import com.eshop.catalog.product.repository.ProductVariantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    Logger LOG = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository, ProductVariantRepository productVariantRepository) {
        this.productRepository = productRepository;
        this.productVariantRepository = productVariantRepository;
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
                saved.getUpdatedAt(), null
        );
    }

    @Transactional(readOnly = true)
    public VariantResponse getBySku(String sku) {
        LOG.info("Product Service-> sku : " + sku);
        ProductVariant saved = productVariantRepository.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Product not found for sku: " + sku));
        VariantResponse variantResponse = new VariantResponse(
                saved.getId(),
                saved.getProduct().getId(),
                saved.getSku(),
                saved.getSize(),
                saved.getDesign(),
                saved.getPriceAmount(),
                saved.getCurrency(),
                saved.getStatus(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
        LOG.info("Product Service-> response : " + variantResponse);
        return variantResponse;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getByProductCode(String productCode) {
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new IllegalArgumentException("Product not found for productCode: " + productCode));
        return toResponse(product);
    }
    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getProductCode(),
                product.getName(),
                product.getBrand(),
                product.getCategory(),
                product.getDescription(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt(), null
             //   product.getVariants().stream()
            //            .map(this::toVariantResponse)
             //           .toList()
        );
        }

}
