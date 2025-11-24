# Multi-stage build for Vespa Hybrid Search Java Application
FROM maven:3.9-eclipse-temurin-11 AS build

WORKDIR /app

# Copy pom.xml first for better caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:11-jre

WORKDIR /app

# Copy built JAR
COPY --from=build /app/target/*.jar app.jar

# Copy Vespa application package (for deployment)
COPY src/main/application /app/application

# Expose port (if needed for any web interface)
EXPOSE 8080

# Default command - can be overridden
CMD ["java", "-jar", "app.jar"]

