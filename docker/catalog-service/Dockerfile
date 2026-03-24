FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# 2. Copy ALL module folders (Maven needs them to resolve the reactor)
COPY api-gateway ./api-gateway
COPY catalog-service ./catalog-service
COPY product-import-worker ./product-import-worker
COPY search-service ./search-service

RUN mvn clean package -pl catalog-service -am -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy the JAR from the 'build' stage above
COPY --from=build /app/target/catalog-service/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]