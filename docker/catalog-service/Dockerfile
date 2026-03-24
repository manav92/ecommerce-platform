FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -pl catalog-service -am -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy the JAR from the 'build' stage above
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]