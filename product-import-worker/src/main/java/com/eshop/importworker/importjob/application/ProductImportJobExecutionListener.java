package com.eshop.importworker.importjob.application;

import com.eshop.importworker.importjob.domain.ImportJobStatus;
import com.eshop.importworker.importjob.domain.ProductImportJob;
import com.eshop.importworker.importjob.repository.ProductImportJobRepository;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.UUID;

@Component
public class ProductImportJobExecutionListener implements JobExecutionListener {
    private final ProductImportJobRepository repository;

    public ProductImportJobExecutionListener(ProductImportJobRepository repository) {
        this.repository = repository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        String jobId = jobExecution.getJobParameters().getString("jobId");
        ProductImportJob job = repository.findById(UUID.fromString(jobId)).orElseThrow();
        job.setStatus(ImportJobStatus.PROCESSING);
        repository.save(job);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobId = jobExecution.getJobParameters().getString("jobId");
        ProductImportJob job = repository.findById(UUID.fromString(jobId)).orElseThrow();
        job.setCompletedAt(Instant.now());

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            job.setStatus(job.getFailedRows() != null && job.getFailedRows() > 0
                    ? ImportJobStatus.PARTIAL_SUCCESS
                    : ImportJobStatus.COMPLETED);
        } else {
            job.setStatus(ImportJobStatus.FAILED);
            job.setFailureReason(
                    jobExecution.getAllFailureExceptions().stream()
                            .findFirst()
                            .map(Throwable::getMessage)
                            .orElse("Batch job failed")
            );
        }

        repository.save(job);
    }
}
