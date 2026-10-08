# ==============================
# Build Stage
# ==============================

FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY . .

# Make Maven Wrapper executable on Linux
RUN chmod +x mvnw

# Build the Spring Boot application
RUN ./mvnw clean package -DskipTests


# ==============================
# Run Stage
# ==============================

FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the generated JAR
COPY --from=build /app/target/*.jar app.jar

# Render provides the PORT environment variable
EXPOSE 10000

# Start Spring Boot
CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT:-10000}"]