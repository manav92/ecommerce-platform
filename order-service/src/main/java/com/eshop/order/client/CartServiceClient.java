package com.eshop.order.client;

import com.eshop.order.dto.CartResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CartServiceClient {

    private final WebClient webClient;

    public CartServiceClient(@Value("${services.cart-service.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public CartResponse getCart(String customerId) {
        return webClient.get()
                .uri("/api/v1/cart/{customerId}", customerId)
                .retrieve()
                .bodyToMono(CartResponse.class)
                .block();
    }

    public void checkout(String customerId) {
        webClient.post()
                .uri("/api/v1/cart/{customerId}/checkout", customerId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void clear(String customerId) {
        webClient.delete()
                .uri("/api/v1/cart/{customerId}", customerId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void abandon(String customerId) {
        webClient.delete()
                .uri("/api/v1/cart/{customerId}/abandon", customerId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
