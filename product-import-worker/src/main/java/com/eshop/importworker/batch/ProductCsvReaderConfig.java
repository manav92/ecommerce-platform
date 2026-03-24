package com.eshop.importworker.batch;

import com.eshop.importworker.batch.model.ProductCsvRow;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class ProductCsvReaderConfig {
    @Bean
    @StepScope
    public FlatFileItemReader<ProductCsvRow> productCsvReader(
            @Value("#{jobParameters['filePath']}") String filePath) {

        FlatFileItemReader<ProductCsvRow> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLinesToSkip(1);

        DefaultLineMapper<ProductCsvRow> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames(
                "productCode",
                "name",
                "brand",
                "category",
                "description",
                "sku",
                "size",
                "design",
                "priceAmount",
                "currency"
        );

        BeanWrapperFieldSetMapper<ProductCsvRow> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ProductCsvRow.class);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        reader.setLineMapper(lineMapper);

        return reader;
    }
}
