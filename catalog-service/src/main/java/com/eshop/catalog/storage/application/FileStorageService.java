package com.eshop.catalog.storage.application;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    StoredFile store(MultipartFile file);

    record StoredFile(String originalFileName, String storedFileName, String absolutePath) {}
}
