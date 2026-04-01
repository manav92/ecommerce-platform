package com.eshop.order.application;

import com.eshop.order.client.CartServiceClient;
import com.eshop.order.client.InventoryServiceClient;
import com.eshop.order.domain.CustomerOrder;
import com.eshop.order.domain.CustomerOrderItem;
import com.eshop.order.domain.OrderStatus;
import com.eshop.order.dto.*;
import com.eshop.order.messaging.OrderEventPublisher;
import com.eshop.order.repository.CustomerOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final CustomerOrderRepository orderRepository;
    private final CartServiceClient cartServiceClient;
    private final InventoryServiceClient inventoryServiceClient;
    private final OrderEventPublisher orderEventPublisher;

    public OrderService(CustomerOrderRepository orderRepository,
                        CartServiceClient cartServiceClient,
                        InventoryServiceClient inventoryServiceClient,
                        OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.cartServiceClient = cartServiceClient;
        this.inventoryServiceClient = inventoryServiceClient;
        this.orderEventPublisher = orderEventPublisher;
    }

    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        CustomerOrder existing = orderRepository.findByIdempotencyKey(request.idempotencyKey()).orElse(null);
        if (existing != null) {
            return toResponse(existing);
        }

        CartResponse cart = cartServiceClient.getCart(request.customerId());

        if (cart.items() == null || cart.items().isEmpty()) {
            throw new IllegalArgumentException("Cannot place order for empty cart");
        }

        CustomerOrder order = new CustomerOrder();
        order.setCustomerId(cart.customerId());
        order.setIdempotencyKey(request.idempotencyKey().trim());
        order.setGrandTotal(cart.grandTotal());
        order.setCurrency(cart.currency());
        order.setStatus(OrderStatus.CREATED);

        for (CartItemResponse cartItem : cart.items()) {
            CustomerOrderItem item = new CustomerOrderItem();
            item.setOrder(order);
            item.setProductCode(cartItem.productCode());
            item.setSku(cartItem.sku());
            item.setProductName(cartItem.productName());
            item.setQuantity(cartItem.quantity());
            item.setUnitPrice(cartItem.unitPrice());
            item.setLineTotal(cartItem.lineTotal());
            order.getItems().add(item);
        }

        CustomerOrder savedOrder = orderRepository.save(order);

        List<String> reservedOnlySkus = new ArrayList<>();
        try {
            for (CartItemResponse item : cart.items()) {
                InventoryReservationResponse reservation = inventoryServiceClient.reserve(new ReserveInventoryRequest(
                        item.sku(),
                        "ORDER",
                        savedOrder.getId().toString(),
                        item.quantity(),
                        15
                ));

                if ("RESERVED".equalsIgnoreCase(reservation.status())) {
                    reservedOnlySkus.add(item.sku());
                }
            }

            savedOrder.setStatus(OrderStatus.INVENTORY_RESERVED);
            orderRepository.save(savedOrder);

            for (CartItemResponse item : cart.items()) {
                inventoryServiceClient.confirm("ORDER", savedOrder.getId().toString(), item.sku());
                reservedOnlySkus.remove(item.sku());
            }

            savedOrder.setStatus(OrderStatus.CONFIRMED);
            CustomerOrder confirmed = orderRepository.save(savedOrder);

            cartServiceClient.checkout(request.customerId());
            cartServiceClient.clear(request.customerId());

            orderEventPublisher.publishOrderPlaced(new OrderPlacedEvent(
                    confirmed.getId(),
                    confirmed.getCustomerId(),
                    confirmed.getGrandTotal(),
                    confirmed.getCurrency(),
                    confirmed.getStatus().name()
            ));

            return toResponse(confirmed);
        } catch (Exception ex) {
            for (String sku : reservedOnlySkus) {
                try {
                    inventoryServiceClient.release("ORDER", savedOrder.getId().toString(), sku);
                } catch (Exception ignored) {
                }
            }

            savedOrder.setStatus(OrderStatus.FAILED);
            savedOrder.setFailureReason(ex.getMessage());
            orderRepository.save(savedOrder);

            try {
                cartServiceClient.abandon(request.customerId());
            } catch (Exception ignored) {
            }

            throw new IllegalArgumentException("Order placement failed: " + ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID orderId) {
        CustomerOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public OrderResponse cancelOrder(UUID orderId) {
        CustomerOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            return toResponse(order);
        }

        order.setStatus(OrderStatus.CANCELLED);
        return toResponse(orderRepository.save(order));
    }

    private OrderResponse toResponse(CustomerOrder order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getGrandTotal(),
                order.getCurrency(),
                order.getFailureReason(),
                order.getItems().stream()
                        .map(item -> new OrderItemResponse(
                                item.getId(),
                                item.getProductCode(),
                                item.getSku(),
                                item.getProductName(),
                                item.getQuantity(),
                                item.getUnitPrice(),
                                item.getLineTotal()
                        ))
                        .toList(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
