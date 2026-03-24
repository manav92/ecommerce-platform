package com.eshop.importworker.batch;

import com.eshop.importworker.batch.model.ProcessedProductRow;
import com.eshop.importworker.catalog.domain.Product;
import com.eshop.importworker.catalog.domain.ProductVariant;
import com.eshop.importworker.catalog.repository.ProductRepository;
import com.eshop.importworker.catalog.repository.ProductVariantRepository;
import com.eshop.importworker.messaging.ProductEventPublisher;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProductCsvRowWriter implements ItemWriter<ProcessedProductRow> {
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final ProductEventPublisher productEventPublisher;
    private final Counter processedRows;

    public ProductCsvRowWriter(ProductRepository productRepository,
                               ProductVariantRepository variantRepository,
                               ProductEventPublisher productEventPublisher,
                               MeterRegistry meterRegistry) {
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.productEventPublisher = productEventPublisher;
        this.processedRows = meterRegistry.counter("product_import_rows_processed_total");
    }

    @Override
    @Transactional
    public void write(Chunk<? extends ProcessedProductRow> chunk) {
        for (ProcessedProductRow row : chunk.getItems()) {
            Product product = productRepository.findByProductCode(row.productCode())
                    .orElseGet(Product::new);

            product.setProductCode(row.productCode());
            product.setName(row.name());
            product.setBrand(row.brand());
            product.setCategory(row.category());
            product.setDescription(row.description());
            Product savedProduct = productRepository.save(product);

            ProductVariant variant = variantRepository.findBySku(row.sku())
                    .orElseGet(ProductVariant::new);

            variant.setProduct(savedProduct);
            variant.setSku(row.sku());
            variant.setSize(row.size());
            variant.setDesign(row.design());
            variant.setPriceAmount(row.priceAmount());
            variant.setCurrency(row.currency());
            variantRepository.save(variant);

            productEventPublisher.publishProductUpserted(row);
            processedRows.increment();
        }
    }
}
