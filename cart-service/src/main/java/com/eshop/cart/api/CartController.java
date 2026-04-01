package com.eshop.cart.api;

import com.eshop.cart.application.CartService;
import com.eshop.cart.dto.AddCartItemRequest;
import com.eshop.cart.dto.CartResponse;
import com.eshop.cart.dto.UpdateCartItemRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse addItem(@Valid @RequestBody AddCartItemRequest request) {
        return service.addItem(request);
    }

    @GetMapping("/{customerId}")
    public CartResponse getCart(@PathVariable String customerId) {
        return service.getCart(customerId);
    }

    @PutMapping("/{customerId}/items/{sku}")
    public CartResponse updateItem(@PathVariable String customerId,
                                   @PathVariable String sku,
                                   @Valid @RequestBody UpdateCartItemRequest request) {
        return service.updateItem(customerId, sku, request);
    }

    @DeleteMapping("/{customerId}/items/{sku}")
    public CartResponse removeItem(@PathVariable String customerId, @PathVariable String sku) {
        return service.removeItem(customerId, sku);
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable String customerId) {
        service.clearCart(customerId);
    }

    @PostMapping("/{customerId}/checkout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void checkout(@PathVariable String customerId) {
        service.checkout(customerId);
    }

    @PostMapping("/{customerId}/abandon")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void abandon(@PathVariable String customerId) {
        service.abandon(customerId);
    }
}
