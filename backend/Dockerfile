# Use the OpenJDK 21 base image
FROM openjdk:21-jdk-slim AS build

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

# Set the working directory
WORKDIR /app

# Copy the Maven build file
COPY pom.xml ./

# Download dependencies (you can also use `./mvnw` if you're using Maven wrapper)
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Expose the port the backend app will run on
EXPOSE 8080

# Command to run the Spring Boot app directly with Maven
CMD ["mvn", "spring-boot:run"]
