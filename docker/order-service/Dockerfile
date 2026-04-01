FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# 2. Copy ALL module folders (Maven needs them to resolve the reactor)
COPY api-gateway ./api-gateway
COPY catalog-service ./catalog-service
COPY product-import-worker ./product-import-worker
COPY common-lib ./common-lib
COPY inventory-service ./inventory-service
COPY cart-service ./cart-service
COPY order-service ./order-service


RUN mvn clean package -pl order-service -am -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy the JAR from the 'build' stage above
COPY --from=build /app/order-service/target/*.jar app.jar
EXPOSE 8087
ENTRYPOINT ["java","-jar","app.jar"]