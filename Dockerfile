# Step 1: Maven aur JDK 22 ke sath code ko build karna
FROM maven:3.9-eclipse-temurin-22 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Step 2: Sirf JRE 22 lekar JAR file ko run karna
FROM eclipse-temurin-22-jre
WORKDIR /app
COPY --from=build /app/target/MyTelegramBotProject-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]