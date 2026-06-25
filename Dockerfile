# Step 1: Maven aur Java 22 ka environment lena
FROM maven:3.9-eclipse-temurin-22 AS build
WORKDIR /app

# Step 2: Saari files copy karna aur build karna
COPY . .
RUN mvn clean package

# Step 3: Dummy Server + Java Bot dono chalana
EXPOSE 8080
# nc (netcat) ka use karke port 8080 par ek loop chala rahe hain taaki Render ko lage port active hai
ENTRYPOINT ["sh", "-c", "(while true; do echo -e 'HTTP/1.1 200 OK\n\n Live' | nc -l -p 8080; done) & java -jar target/mybot-jar-with-dependencies.jar"]
