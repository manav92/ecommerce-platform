package com.eshop.importworker.messaging;

import com.eshop.importworker.batch.model.ProcessedProductRow;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProductEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishProductUpserted(ProcessedProductRow row) {
        kafkaTemplate.send("catalog.product.upserted", row.productCode(), Map.of(
                "productCode", row.productCode(),
                "sku", row.sku(),
                "name", row.name(),
                "brand", row.brand() == null ? "" : row.brand(),
                "category", row.category() == null ? "" : row.category(),
                "description", row.description() == null ? "" : row.description(),
                "size", row.size() == null ? "" : row.size(),
                "design", row.design() == null ? "" : row.design(),
                "priceAmount", row.priceAmount() == null ? "0" : row.priceAmount().toString(),
                "currency", row.currency()
        ));
    }
}
