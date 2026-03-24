package com.eshop.catalog.storage.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {
    private final Path uploadRoot;

    public LocalFileStorageService(@Value("${app.storage.upload-dir:uploads}") String uploadDir) throws IOException {
        this.uploadRoot = Path.of(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadRoot);
    }

    @Override
    public StoredFile store(MultipartFile file) {
        try {
            String originalName = StringUtils.cleanPath(file.getOriginalFilename());
            String storedName = UUID.randomUUID() + "-" + originalName;
            Path target = uploadRoot.resolve(storedName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return new StoredFile(originalName, storedName, target.toString());
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to store file", ex);
        }
    }
}
