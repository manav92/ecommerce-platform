package com.eshop.inventory.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "inventory_items",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_inventory_item_sku", columnNames = "sku")
        })
public class InventoryItem {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String productCode;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private Integer totalQuantity = 0;

    @Column(nullable = false)
    private Integer reservedQuantity = 0;

    @Version
    private Long version;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public Long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public int getAvailableQuantity() {
        return totalQuantity - reservedQuantity;
    }
}
