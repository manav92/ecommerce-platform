package com.eshop.importworker.batch;

import com.eshop.importworker.batch.model.ProcessedProductRow;
import com.eshop.importworker.batch.model.ProductCsvRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ProductCsvRowProcessor implements ItemProcessor<ProductCsvRow, ProcessedProductRow> {

    Logger Log = LoggerFactory.getLogger(ProductCsvRowProcessor.class);
    @Override
    public ProcessedProductRow process(ProductCsvRow item) {
        if (isBlank(item.getProductCode()) || isBlank(item.getName()) || isBlank(item.getSku())) {
            throw new IllegalArgumentException("productCode, name and sku are required");
        }
        ProcessedProductRow processedProductRow = new ProcessedProductRow(
                item.getProductCode().trim(),
                item.getName().trim(),
                trim(item.getBrand()),
                trim(item.getCategory()),
                trim(item.getDescription()),
                item.getSku().trim(),
                trim(item.getSize()),
                trim(item.getDesign()),
                item.getPriceAmount(),
                item.getCurrency() == null || item.getCurrency().isBlank() ? "INR" : item.getCurrency().trim()
        );
        Log.info("ProcessedProductRow:" + processedProductRow );

        return processedProductRow;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
