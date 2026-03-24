package com.eshop.catalog.importjob.repository;

import com.eshop.catalog.importjob.domain.ImportJobStatus;
import com.eshop.catalog.importjob.domain.ProductImportJob;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProductImportJobRepository extends JpaRepository<ProductImportJob, UUID> {
    List<ProductImportJob> findTop10ByStatusOrderBySubmittedAtAsc(ImportJobStatus status);
}
