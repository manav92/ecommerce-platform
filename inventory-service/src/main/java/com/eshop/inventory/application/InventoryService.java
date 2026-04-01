package com.eshop.inventory.application;

import com.eshop.inventory.domain.InventoryItem;
import com.eshop.inventory.domain.InventoryReservation;
import com.eshop.inventory.domain.InventoryReservationStatus;
import com.eshop.inventory.dto.InventoryItemResponse;
import com.eshop.inventory.dto.InventoryReservationResponse;
import com.eshop.inventory.dto.ReserveInventoryRequest;
import com.eshop.inventory.dto.UpsertInventoryRequest;
import com.eshop.inventory.repository.InventoryItemRepository;
import com.eshop.inventory.repository.InventoryReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class InventoryService {

    private final InventoryItemRepository itemRepository;
    private final InventoryReservationRepository reservationRepository;

    public InventoryService(InventoryItemRepository itemRepository,
                            InventoryReservationRepository reservationRepository) {
        this.itemRepository = itemRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public InventoryItemResponse upsert(UpsertInventoryRequest request) {
        InventoryItem item = itemRepository.findBySku(request.sku())
                .orElseGet(InventoryItem::new);

        item.setProductCode(request.productCode().trim());
        item.setSku(request.sku().trim());

        if (request.totalQuantity() < item.getReservedQuantity()) {
            throw new IllegalArgumentException("totalQuantity cannot be less than reservedQuantity");
        }

        item.setTotalQuantity(request.totalQuantity());
        return toItemResponse(itemRepository.save(item));
    }

    @Transactional(readOnly = true)
    public InventoryItemResponse getBySku(String sku) {
        InventoryItem item = itemRepository.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Inventory item not found for sku: " + sku));
        return toItemResponse(item);
    }

    @Transactional
    public InventoryReservationResponse reserve(ReserveInventoryRequest request) {
        InventoryItem item = itemRepository.findBySku(request.sku())
                .orElseThrow(() -> new IllegalArgumentException("Inventory item not found for sku: " + request.sku()));

        if (item.getAvailableQuantity() < request.quantity()) {
            throw new IllegalArgumentException("Insufficient inventory for sku: " + request.sku());
        }

        InventoryReservation existing = reservationRepository
                .findByReferenceTypeAndReferenceIdAndSku(
                        request.referenceType(),
                        request.referenceId(),
                        request.sku()
                )
                .orElse(null);

        if (existing != null && existing.getStatus() == InventoryReservationStatus.RESERVED) {
            return toReservationResponse(existing);
        }

        item.setReservedQuantity(item.getReservedQuantity() + request.quantity());
        itemRepository.save(item);

        InventoryReservation reservation = new InventoryReservation();
        reservation.setSku(request.sku().trim());
        reservation.setReferenceType(request.referenceType().trim());
        reservation.setReferenceId(request.referenceId().trim());
        reservation.setQuantity(request.quantity());
        reservation.setStatus(InventoryReservationStatus.RESERVED);
        reservation.setExpiresAt(Instant.now().plus(request.reservationTtlMinutes(), ChronoUnit.MINUTES));

        return toReservationResponse(reservationRepository.save(reservation));
    }

    @Transactional
    public InventoryReservationResponse confirm(String referenceType, String referenceId, String sku) {
        InventoryReservation reservation = reservationRepository
                .findByReferenceTypeAndReferenceIdAndSku(referenceType, referenceId, sku)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (reservation.getStatus() != InventoryReservationStatus.RESERVED) {
            throw new IllegalArgumentException("Reservation is not in RESERVED state");
        }

        InventoryItem item = itemRepository.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Inventory item not found for sku: " + sku));

        item.setReservedQuantity(item.getReservedQuantity() - reservation.getQuantity());
        item.setTotalQuantity(item.getTotalQuantity() - reservation.getQuantity());
        itemRepository.save(item);

        reservation.setStatus(InventoryReservationStatus.CONFIRMED);
        return toReservationResponse(reservationRepository.save(reservation));
    }

    @Transactional
    public InventoryReservationResponse release(String referenceType, String referenceId, String sku) {
        InventoryReservation reservation = reservationRepository
                .findByReferenceTypeAndReferenceIdAndSku(referenceType, referenceId, sku)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (reservation.getStatus() != InventoryReservationStatus.RESERVED) {
            throw new IllegalArgumentException("Reservation is not in RESERVED state");
        }

        InventoryItem item = itemRepository.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Inventory item not found for sku: " + sku));

        item.setReservedQuantity(item.getReservedQuantity() - reservation.getQuantity());
        itemRepository.save(item);

        reservation.setStatus(InventoryReservationStatus.RELEASED);
        return toReservationResponse(reservationRepository.save(reservation));
    }

    @Transactional
    @Scheduled(fixedDelay = 60000)
    public void expireReservations() {
        var expiredReservations = reservationRepository.findByStatusAndExpiresAtBefore(
                InventoryReservationStatus.RESERVED,
                Instant.now()
        );

        for (InventoryReservation reservation : expiredReservations) {
            InventoryItem item = itemRepository.findBySku(reservation.getSku()).orElse(null);
            if (item != null) {
                item.setReservedQuantity(item.getReservedQuantity() - reservation.getQuantity());
                itemRepository.save(item);
            }
            reservation.setStatus(InventoryReservationStatus.EXPIRED);
            reservationRepository.save(reservation);
        }
    }

    private InventoryItemResponse toItemResponse(InventoryItem item) {
        return new InventoryItemResponse(
                item.getId(),
                item.getProductCode(),
                item.getSku(),
                item.getTotalQuantity(),
                item.getReservedQuantity(),
                item.getAvailableQuantity(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }

    private InventoryReservationResponse toReservationResponse(InventoryReservation reservation) {
        return new InventoryReservationResponse(
                reservation.getId(),
                reservation.getSku(),
                reservation.getReferenceType(),
                reservation.getReferenceId(),
                reservation.getQuantity(),
                reservation.getStatus(),
                reservation.getCreatedAt(),
                reservation.getExpiresAt(),
                reservation.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<InventoryReservationResponse> getReservations(String referenceType, String referenceId) {
        return reservationRepository.findByReferenceTypeAndReferenceId(referenceType, referenceId)
                .stream()
                .map(this::toReservationResponse)
                .toList();
    }

}
