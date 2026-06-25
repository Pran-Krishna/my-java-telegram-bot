# Step 1: Maven aur JDK 22 ke sath code ko build karna
FROM maven:3.9-eclipse-temurin-22 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Step 2: Exact JRE 22 image ka use karna jo Docker ko pakka mileygi
FROM eclipse-temurin:22-jre-alpine
WORKDIR /app
COPY --from=build /app/target/MyTelegramBotProject-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
