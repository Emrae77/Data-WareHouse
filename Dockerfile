# Use a base image with Java 17 installed
# syntax=docker/dockerfile:1
FROM eclipse-temurin:17-jdk-jammy

RUN mkdir -p /usr/local/progresssoft
ADD target/progressSoft-0.0.1-SNAPSHOT.jar /usr/local/progresssoft/progress-soft.jar

# Specify the command to run your Java application
CMD ["java", "-jar", "/usr/local/progresssoft/progress-soft.jar"]
