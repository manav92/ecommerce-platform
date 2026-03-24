package com.eshop.importworker.importjob.repository;

import com.eshop.importworker.importjob.domain.ImportJobStatus;
import com.eshop.importworker.importjob.domain.ProductImportJob;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProductImportJobRepository extends JpaRepository<ProductImportJob, UUID> {
    List<ProductImportJob> findTop5ByStatusOrderBySubmittedAtAsc(ImportJobStatus status);
}
