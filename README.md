# Ecommerce Platform Skeleton

This zip contains the generated codebase skeleton up to the current phase:
- common-lib
- api-gateway
- catalog-service
- product-import-worker
- search-service
- docker-compose, Prometheus, Grafana provisioning
- sample CSV data

Notes:
- This is a scaffold, not a fully production-ready build.
- Some listeners and batch error handling are partial and need refinement.
- JPA uses ddl-auto=update for now; Flyway is the next recommended step.

Phase 4 additions:
- inventory-service
- cart-service
- Flyway added to catalog and inventory
- richer search indexing event payload
- basic import row metrics

Phase 5 additions:
- order-service with checkout, history, reorder
- notification-service consuming order.placed
- gateway routes and Prometheus targets updated
