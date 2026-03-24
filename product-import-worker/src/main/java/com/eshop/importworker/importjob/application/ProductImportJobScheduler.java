package com.eshop.importworker.importjob.application;

import com.eshop.importworker.importjob.domain.ImportJobStatus;
import com.eshop.importworker.importjob.domain.ProductImportJob;
import com.eshop.importworker.importjob.repository.ProductImportJobRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;

@Component
public class ProductImportJobScheduler {
    private final ProductImportJobRepository repository;
    private final JobLauncher jobLauncher;
    private final Job productImportJob;

    public ProductImportJobScheduler(ProductImportJobRepository repository,
                                     JobLauncher jobLauncher,
                                     Job productImportJob) {
        this.repository = repository;
        this.jobLauncher = jobLauncher;
        this.productImportJob = productImportJob;
    }

    @Scheduled(fixedDelayString = "${import.poll-interval-ms:15000}")
    @Transactional
    public void pollAndLaunch() throws Exception {
        List<ProductImportJob> jobs = repository.findTop5ByStatusOrderBySubmittedAtAsc(ImportJobStatus.RECEIVED);

        for (ProductImportJob job : jobs) {
            job.setStatus(ImportJobStatus.QUEUED);
            job.setStartedAt(Instant.now());
            repository.save(job);

            jobLauncher.run(
                    productImportJob,
                    new JobParametersBuilder()
                            .addString("jobId", job.getId().toString())
                            .addString("filePath", job.getFilePath())
                            .addLong("time", System.currentTimeMillis())
                            .toJobParameters()
            );
        }
    }
}
