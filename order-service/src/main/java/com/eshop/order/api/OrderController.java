package com.eshop.order.api;

import com.eshop.order.application.OrderService;
import com.eshop.order.dto.OrderResponse;
import com.eshop.order.dto.PlaceOrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        return service.placeOrder(request);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable UUID orderId) {
        return service.getOrder(orderId);
    }

    @GetMapping("/customer/{customerId}")
    public List<OrderResponse> getOrdersByCustomer(@PathVariable String customerId) {
        return service.getOrdersByCustomer(customerId);
    }

    @PostMapping("/{orderId}/cancel")
    public OrderResponse cancel(@PathVariable UUID orderId) {
        return service.cancelOrder(orderId);
    }
}
