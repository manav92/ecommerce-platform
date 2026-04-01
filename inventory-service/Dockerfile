FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# 2. Copy ALL module folders (Maven needs them to resolve the reactor)
COPY api-gateway ./api-gateway
COPY catalog-service ./catalog-service
COPY product-import-worker ./product-import-worker
COPY common-lib ./common-lib
COPY inventory-service ./inventory-service
COPY order-service ./order-service
COPY cart-service ./cart-service

RUN mvn clean package -pl inventory-service -am -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy the JAR from the 'build' stage above
COPY --from=build /app/inventory-service/target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","app.jar"]