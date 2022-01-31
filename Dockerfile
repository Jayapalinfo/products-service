# Build stage
FROM maven:3.6.0-jdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src /app/src
RUN mvn clean install -Dmaven.test.skip

# Package stage
From openjdk:11
WORKDIR /app
copy --from=build /app/target/products-service-0.0.1-SNAPSHOT.jar products-service.jar
CMD ["java","-jar","products-service.jar"]