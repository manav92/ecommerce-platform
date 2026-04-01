package com.eshop.inventory.api;

import com.eshop.inventory.application.InventoryService;
import com.eshop.inventory.dto.InventoryItemResponse;
import com.eshop.inventory.dto.InventoryReservationResponse;
import com.eshop.inventory.dto.ReserveInventoryRequest;
import com.eshop.inventory.dto.UpsertInventoryRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryItemResponse upsert(@Valid @RequestBody UpsertInventoryRequest request) {
        return service.upsert(request);
    }

    @GetMapping("/sku/{sku}")
    public InventoryItemResponse getBySku(@PathVariable String sku) {
        return service.getBySku(sku);
    }

    @PostMapping("/reserve")
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryReservationResponse reserve(@Valid @RequestBody ReserveInventoryRequest request) {
        return service.reserve(request);
    }

    @PostMapping("/confirm")
    public InventoryReservationResponse confirm(@RequestParam String referenceType,
                                                @RequestParam String referenceId,
                                                @RequestParam String sku) {
        return service.confirm(referenceType, referenceId, sku);
    }

    @PostMapping("/release")
    public InventoryReservationResponse release(@RequestParam String referenceType,
                                                @RequestParam String referenceId,
                                                @RequestParam String sku) {
        return service.release(referenceType, referenceId, sku);
    }

    @GetMapping("/reservations")
    public List<InventoryReservationResponse> getReservations(@RequestParam String referenceType,
                                                              @RequestParam String referenceId) {
        return service.getReservations(referenceType, referenceId);
    }
}
