package com.eshop.importworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.eshop")
@EnableScheduling
public class ProductImportWorkerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductImportWorkerApplication.class, args);
    }
}
