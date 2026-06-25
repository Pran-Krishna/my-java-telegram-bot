# Step 1: Maven aur JDK 22 ke sath code ko build karna
FROM maven:3.9-eclipse-temurin-22 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Step 2: Exact JRE 22 image ka use karna
FROM eclipse-temurin:22-jre-alpine
WORKDIR /app
# Yahan humne exact naam ki jagah *-jar-with-dependencies.jar kar diya ha
COPY --from=build /app/target/*-jar-with-dependencies.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
