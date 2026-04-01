# E-Commerce Platform

A microservices-based e-commerce platform built with Spring Boot, Kafka, PostgreSQL, Redis, Elasticsearch, Docker, and Spring Batch.

## Modules

- common-lib
- catalog-service
- product-import-worker
- search-service
- api-gateway
- inventory-service
- cart-service
- order-service

## Architecture

- catalog-service manages products, variants, and import job creation.
- product-import-worker polls import jobs, processes CSV files with Spring Batch, upserts catalog data, and publishes Kafka events.
- search-service consumes product upsert events and indexes searchable documents in Elasticsearch.
- inventory-service manages stock and inventory reservations.
- cart-service stores carts in Redis and fetches live SKU pricing from catalog-service.
- order-service creates orders, coordinates inventory reservation, and publishes order events.
- api-gateway routes external requests to backend services.

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Cloud Gateway
- Spring Data JPA
- Spring Data Redis
- Spring Batch
- PostgreSQL
- Redis
- Apache Kafka
- Elasticsearch
- Docker Compose
- Micrometer + Prometheus

## Run Infrastructure
1. Build jars:
   mvn clean package -DskipTests

2. Start all containers:
docker compose up -d
