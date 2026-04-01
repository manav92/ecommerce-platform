package com.eshop.order.repository;

import com.eshop.order.domain.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, UUID> {
    List<CustomerOrder> findByCustomerIdOrderByCreatedAtDesc(String customerId);
    Optional<CustomerOrder> findByIdempotencyKey(String idempotencyKey);
}
