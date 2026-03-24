package com.eshop.importworker.catalog.repository;

import com.eshop.importworker.catalog.domain.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {
    Optional<ProductVariant> findBySku(String sku);
}
