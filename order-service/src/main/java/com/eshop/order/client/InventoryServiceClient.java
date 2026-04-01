package com.eshop.order.client;

import com.eshop.order.dto.InventoryReservationResponse;
import com.eshop.order.dto.ReserveInventoryRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class InventoryServiceClient {

    private final WebClient webClient;

    public InventoryServiceClient(@Value("${services.inventory-service.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public InventoryReservationResponse reserve(ReserveInventoryRequest request) {
        return webClient.post()
                .uri("/api/v1/inventory/reserve")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(InventoryReservationResponse.class)
                .block();
    }

    public void confirm(String referenceType, String referenceId, String sku) {
        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/inventory/confirm")
                        .queryParam("referenceType", referenceType)
                        .queryParam("referenceId", referenceId)
                        .queryParam("sku", sku)
                        .build())
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void release(String referenceType, String referenceId, String sku) {
        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/inventory/release")
                        .queryParam("referenceType", referenceType)
                        .queryParam("referenceId", referenceId)
                        .queryParam("sku", sku)
                        .build())
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
