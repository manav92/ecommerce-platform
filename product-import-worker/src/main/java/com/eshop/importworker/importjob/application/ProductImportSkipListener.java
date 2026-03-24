package com.eshop.importworker.importjob.application;

import com.eshop.importworker.batch.model.ProductCsvRow;
import com.eshop.importworker.importjob.domain.ProductImportError;
import com.eshop.importworker.importjob.domain.ProductImportJob;
import com.eshop.importworker.importjob.repository.ProductImportErrorRepository;
import com.eshop.importworker.importjob.repository.ProductImportJobRepository;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class ProductImportSkipListener implements SkipListener<ProductCsvRow, Object> {
    private final ProductImportErrorRepository errorRepository;
    private final ProductImportJobRepository jobRepository;

    public ProductImportSkipListener(ProductImportErrorRepository errorRepository,
                                     ProductImportJobRepository jobRepository) {
        this.errorRepository = errorRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    public void onSkipInProcess(ProductCsvRow item, Throwable t) {}

    @Override
    public void onSkipInWrite(Object item, Throwable t) {}

    @Override
    public void onSkipInRead(Throwable t) {}

    public void saveError(JobParameters jobParameters, Integer rowNumber, String rawRecord, Throwable t) {
        UUID jobId = UUID.fromString(jobParameters.getString("jobId"));

        ProductImportError error = new ProductImportError();
        error.setJobId(jobId);
        error.setRowNumber(rowNumber);
        error.setRawRecord(rawRecord);
        error.setErrorCode("ROW_IMPORT_ERROR");
        error.setErrorMessage(t.getMessage());
        errorRepository.save(error);

        ProductImportJob job = jobRepository.findById(jobId).orElseThrow();
        job.setFailedRows((job.getFailedRows() == null ? 0 : job.getFailedRows()) + 1);
        jobRepository.save(job);
    }
}
