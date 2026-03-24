package com.eshop.catalog.importjob.domain;

public enum ImportJobStatus {
    RECEIVED, QUEUED, PROCESSING, COMPLETED, PARTIAL_SUCCESS, FAILED, RETRY_REQUESTED
}
