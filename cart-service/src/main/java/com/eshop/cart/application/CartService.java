package com.eshop.cart.application;

import com.eshop.cart.client.CatalogServiceClient;
import com.eshop.cart.domain.Cart;
import com.eshop.cart.domain.CartItem;
import com.eshop.cart.domain.CartStatus;
import com.eshop.cart.dto.*;
import com.eshop.cart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final RedisTemplate<String, Cart> redisTemplate;
    private final CatalogServiceClient catalogServiceClient;
    private final long cartTtlMinutes;

    public CartService(CartRepository cartRepository,
                       RedisTemplate<String, Cart> redisTemplate,
                       CatalogServiceClient catalogServiceClient,
                       @Value("${services.cart.ttl-minutes}") long cartTtlMinutes) {
        this.cartRepository = cartRepository;
        this.redisTemplate = redisTemplate;
        this.catalogServiceClient = catalogServiceClient;
        this.cartTtlMinutes = cartTtlMinutes;
    }

    public CartResponse addItem(AddCartItemRequest request) {
        Cart cart = cartRepository.findById(request.customerId().trim()).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setCustomerId(request.customerId().trim());
            newCart.setStatus(CartStatus.ACTIVE);
            return newCart;
        });

        ensureActive(cart);

        CartItem item = cart.getItems().stream()
                .filter(existing -> existing.getSku().equalsIgnoreCase(request.sku()))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setId(UUID.randomUUID());
                    newItem.setCreatedAt(Instant.now());
                    cart.getItems().add(newItem);
                    return newItem;
                });

        item.setProductCode(request.productCode().trim());
        item.setSku(request.sku().trim());
        item.setProductName(request.productName().trim());
        item.setQuantity((item.getQuantity() == null ? 0 : item.getQuantity()) + request.quantity());
        item.setUpdatedAt(Instant.now());

        cart.setUpdatedAt(Instant.now());
        saveWithTtl(cart);

        return toResponse(cart);
    }

    public CartResponse getCart(String customerId) {
        Cart cart = cartRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Active cart not found for customerId: " + customerId));
        refreshTtl(cart.getCustomerId());
        return toResponse(cart);
    }

    public CartResponse updateItem(String customerId, String sku, UpdateCartItemRequest request) {
        Cart cart = cartRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Active cart not found for customerId: " + customerId));

        ensureActive(cart);

        CartItem item = cart.getItems().stream()
                .filter(existing -> existing.getSku().equalsIgnoreCase(sku))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found for sku: " + sku));

        item.setQuantity(request.quantity());
        item.setUpdatedAt(Instant.now());
        cart.setUpdatedAt(Instant.now());

        saveWithTtl(cart);
        return toResponse(cart);
    }

    public CartResponse removeItem(String customerId, String sku) {
        Cart cart = cartRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Active cart not found for customerId: " + customerId));

        ensureActive(cart);

        boolean removed = cart.getItems().removeIf(item -> item.getSku().equalsIgnoreCase(sku));
        if (!removed) {
            throw new IllegalArgumentException("Cart item not found for sku: " + sku);
        }

        cart.setUpdatedAt(Instant.now());
        saveWithTtl(cart);
        return toResponse(cart);
    }

    public void clearCart(String customerId) {
        cartRepository.deleteById(customerId);
    }

    public void checkout(String customerId) {
        Cart cart = cartRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for customerId: " + customerId));

        ensureActive(cart);

        cart.setStatus(CartStatus.CHECKED_OUT);
        cart.setUpdatedAt(Instant.now());
        cartRepository.save(cart);

        redisTemplate.expire(customerId, Duration.ofMinutes(10));
    }

    public void abandon(String customerId) {
        Cart cart = cartRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for customerId: " + customerId));

        cart.setStatus(CartStatus.ABANDONED);
        cart.setUpdatedAt(Instant.now());
        cartRepository.save(cart);

        redisTemplate.expire(customerId, Duration.ofMinutes(60));
    }

    private void ensureActive(Cart cart) {
        if (cart.getStatus() != CartStatus.ACTIVE) {
            throw new IllegalArgumentException("Cart is not ACTIVE");
        }
    }

    private void saveWithTtl(Cart cart) {
        cartRepository.save(cart);
        refreshTtl(cart.getCustomerId());
    }

    private void refreshTtl(String customerId) {
        redisTemplate.expire(customerId, Duration.ofMinutes(cartTtlMinutes));
    }

    private CartResponse toResponse(Cart cart) {
        var items = new ArrayList<CartItemResponse>();
        BigDecimal grandTotal = BigDecimal.ZERO;
        String currency = "INR";
        int totalItems = 0;

        for (CartItem item : cart.getItems()) {
            CatalogVariantResponse variant = catalogServiceClient.getVariantBySku(item.getSku());
            BigDecimal lineTotal = variant.priceAmount().multiply(BigDecimal.valueOf(item.getQuantity()));

            items.add(new CartItemResponse(
                    item.getId(),
                    item.getProductCode(),
                    item.getSku(),
                    item.getProductName(),
                    item.getQuantity(),
                    variant.priceAmount(),
                    lineTotal,
                    variant.currency()
            ));

            grandTotal = grandTotal.add(lineTotal);
            totalItems += item.getQuantity();
            currency = variant.currency();
        }

        return new CartResponse(
                cart.getCustomerId(),
                cart.getStatus(),
                items,
                totalItems,
                grandTotal,
                currency,
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }
}
