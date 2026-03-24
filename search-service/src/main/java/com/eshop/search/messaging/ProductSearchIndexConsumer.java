package com.eshop.search.messaging;

import com.eshop.search.document.ProductDocument;
import com.eshop.search.dto.ProductUpsertedEvent;
import com.eshop.search.repository.ProductSearchRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductSearchIndexConsumer {
    private final ProductSearchRepository repository;

    public ProductSearchIndexConsumer(ProductSearchRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "catalog.product.upserted", groupId = "search-service")
    public void onProductUpserted(ProductUpsertedEvent event) {
        ProductDocument doc = new ProductDocument();
        doc.setId(event.sku());
        doc.setProductCode(event.productCode());
        doc.setSku(event.sku());
        doc.setName(event.name());
        doc.setBrand(event.brand());
        doc.setCategory(event.category());
        doc.setDescription(event.description());
        doc.setSize(event.size());
        doc.setDesign(event.design());
        doc.setPriceAmount(event.priceAmount());
        doc.setCurrency(event.currency());
        repository.save(doc);
    }
}
