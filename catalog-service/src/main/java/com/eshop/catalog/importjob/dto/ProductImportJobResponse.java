package com.eshop.catalog.importjob.dto;

import com.eshop.catalog.importjob.domain.ImportJobStatus;
import java.time.Instant;
import java.util.UUID;

public record ProductImportJobResponse(
        UUID jobId,
        ImportJobStatus status,
        String fileName,
        String filePath,
        String uploadedBy,
        Integer totalRows,
        Integer processedRows,
        Integer successRows,
        Integer failedRows,
        String failureReason,
        Instant submittedAt,
        Instant startedAt,
        Instant completedAt
) {}
