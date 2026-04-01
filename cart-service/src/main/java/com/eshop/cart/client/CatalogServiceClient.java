package com.eshop.cart.client;

import com.eshop.cart.dto.CatalogProductResponse;
import com.eshop.cart.dto.CatalogVariantResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

@Component
public class CatalogServiceClient {

    private final WebClient webClient;

    Logger LOG = LoggerFactory.getLogger(CatalogServiceClient.class);

    public CatalogServiceClient(@Value("${services.catalog-service.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public CatalogVariantResponse getVariantBySku(String sku) {
        CatalogVariantResponse product = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/products/sku/"+ sku)
                        .build())
                .retrieve()
                .bodyToFlux(CatalogVariantResponse.class)
                .blockFirst();

        LOG.info("CatalogServiceClient Response: " + product);

        if (product == null) {
            throw new IllegalArgumentException("Catalog response is empty for sku: " + sku);
        }

         return new CatalogVariantResponse(product.id(),product.sku(),product.size(), product.design(), product.priceAmount(), product.currency()
        ,product.status());
    }
}
