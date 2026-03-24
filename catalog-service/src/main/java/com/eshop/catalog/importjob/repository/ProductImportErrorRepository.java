package com.eshop.catalog.importjob.repository;

import com.eshop.catalog.importjob.domain.ProductImportError;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProductImportErrorRepository extends JpaRepository<ProductImportError, UUID> {
    List<ProductImportError> findByJobId(UUID jobId);
}
