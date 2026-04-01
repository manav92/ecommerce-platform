package com.eshop.inventory.repository;

import com.eshop.inventory.domain.InventoryReservation;
import com.eshop.inventory.domain.InventoryReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryReservationRepository extends JpaRepository<InventoryReservation, UUID> {
    Optional<InventoryReservation> findByReferenceTypeAndReferenceIdAndSku(
            String referenceType,
            String referenceId,
            String sku
    );

    List<InventoryReservation> findByStatusAndExpiresAtBefore(
            InventoryReservationStatus status,
            Instant expiresAt
    );

    List<InventoryReservation> findByReferenceTypeAndReferenceId(
            String referenceType,
            String referenceId
    );
}
