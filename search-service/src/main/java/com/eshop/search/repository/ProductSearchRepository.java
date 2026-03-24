package com.eshop.search.repository;

import com.eshop.search.document.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {
    List<ProductDocument> findByNameContainingOrBrandContainingOrCategoryContaining(
            String name,
            String brand,
            String category
    );
}
