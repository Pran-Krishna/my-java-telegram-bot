# Step 1: Maven aur Java 22 ka environment lena
FROM maven:3.9-eclipse-temurin-22
WORKDIR /app

# Step 2: Saari files copy karna aur build karna
COPY . .
RUN mvn clean package

# Render ko khush rakhne ke liye port expose
EXPOSE 8080

# Step 3: Shell ka use karke dynamic tarike se JAR file run karna
CMD ["sh", "-c", "java -jar target/*-jar-with-dependencies.jar || java -jar target/*.jar"]
