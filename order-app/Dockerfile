FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /workspace

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

RUN mkdir -p /logs/order-app

COPY --from=builder /workspace/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]