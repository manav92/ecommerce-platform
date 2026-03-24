package com.eshop.importworker.importjob.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "product_import_errors")
public class ProductImportError {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID jobId;

    @Column(nullable = false)
    private Integer rowNumber;

    @Column(length = 4000)
    private String rawRecord;

    @Column(nullable = false)
    private String errorCode;

    @Column(nullable = false, length = 1000)
    private String errorMessage;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public void setJobId(UUID jobId) { this.jobId = jobId; }
    public void setRowNumber(Integer rowNumber) { this.rowNumber = rowNumber; }
    public void setRawRecord(String rawRecord) { this.rawRecord = rawRecord; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
