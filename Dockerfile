# Step 1: Build stage
FROM maven:3.9-eclipse-temurin-22 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Step 2: Run stage
FROM eclipse-temurin:22-jre-alpine
WORKDIR /app
COPY --from=build /app/target/mybot-jar-with-dependencies.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
