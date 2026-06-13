# -----------------------------
# Build Stage
# -----------------------------
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /workspace

COPY . .

RUN mvn clean package -DskipTests

# -----------------------------
# Runtime Stage
# -----------------------------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Create log directories
RUN mkdir -p /logs/payment-app

# Copy generated jar
COPY --from=builder /workspace/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]