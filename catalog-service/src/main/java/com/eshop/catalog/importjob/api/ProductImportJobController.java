package com.eshop.catalog.importjob.api;

import com.eshop.catalog.importjob.application.ProductImportJobService;
import com.eshop.catalog.importjob.dto.ProductImportJobResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/product-imports")
public class ProductImportJobController {
    private final ProductImportJobService service;

    public ProductImportJobController(ProductImportJobService service) {
        this.service = service;
    }

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.ACCEPTED)    public ProductImportJobResponse create(@RequestPart("file") MultipartFile file,
                                           @RequestParam("uploadedBy") String uploadedBy) {
        return service.create(file, uploadedBy);
    }

    @GetMapping("/{jobId}")
    public ProductImportJobResponse get(@PathVariable UUID jobId) {
        return service.get(jobId);
    }
}
