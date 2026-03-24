package com.eshop.importworker.importjob.repository;

import com.eshop.importworker.importjob.domain.ProductImportError;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProductImportErrorRepository extends JpaRepository<ProductImportError, UUID> {}
