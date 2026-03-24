package com.eshop.importworker.batch;

import com.eshop.importworker.batch.model.ProcessedProductRow;
import com.eshop.importworker.batch.model.ProductCsvRow;
import com.eshop.importworker.importjob.application.ProductImportJobExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ProductImportJobConfig {
    @Bean
    public Job productImportJob(JobRepository jobRepository,
                                Step productImportStep,
                                ProductImportJobExecutionListener listener) {
        return new JobBuilder("productImportJob", jobRepository)
                .listener(listener)
                .start(productImportStep)
                .build();
    }

    @Bean
    public Step productImportStep(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  FlatFileItemReader<ProductCsvRow> productCsvReader,
                                  ProductCsvRowProcessor processor,
                                  ProductCsvRowWriter writer) {
        return new StepBuilder("productImportStep", jobRepository)
                .<ProductCsvRow, ProcessedProductRow>chunk(50, transactionManager)
                .reader(productCsvReader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(1000)
                .build();
    }
}
