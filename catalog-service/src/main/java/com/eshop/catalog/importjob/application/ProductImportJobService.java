package com.eshop.catalog.importjob.application;

import com.eshop.catalog.importjob.domain.ImportJobStatus;
import com.eshop.catalog.importjob.domain.ProductImportJob;
import com.eshop.catalog.importjob.dto.ProductImportJobResponse;
import com.eshop.catalog.importjob.repository.ProductImportJobRepository;
import com.eshop.catalog.storage.application.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Service
public class ProductImportJobService {
    private final ProductImportJobRepository repository;
    private final FileStorageService fileStorageService;

    public ProductImportJobService(ProductImportJobRepository repository,
                                   FileStorageService fileStorageService) {
        this.repository = repository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public ProductImportJobResponse create(MultipartFile file, String uploadedBy) {
        FileStorageService.StoredFile stored = fileStorageService.store(file);

        ProductImportJob job = new ProductImportJob();
        job.setFileName(stored.originalFileName());
        job.setFilePath(stored.absolutePath());
        job.setUploadedBy(uploadedBy);
        job.setStatus(ImportJobStatus.RECEIVED);

        ProductImportJob saved = repository.save(job);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProductImportJobResponse get(UUID jobId) {
        ProductImportJob job = repository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Import job not found"));
        return toResponse(job);
    }

    private ProductImportJobResponse toResponse(ProductImportJob job) {
        return new ProductImportJobResponse(
                job.getId(),
                job.getStatus(),
                job.getFileName(),
                job.getFilePath(),
                job.getUploadedBy(),
                job.getTotalRows(),
                job.getProcessedRows(),
                job.getSuccessRows(),
                job.getFailedRows(),
                job.getFailureReason(),
                job.getSubmittedAt(),
                job.getStartedAt(),
                job.getCompletedAt()
        );
    }
}
