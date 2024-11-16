# Use maven to build project

FROM maven:3.9.9 AS build
WORKDIR /app
COPY ./discoveryserver ./discoveryserver
WORKDIR /app/discoveryserver
RUN mvn clean package -DskipTests

# Use open jdk to run
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/discoveryserver/target/*.jar discoveryserver.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "discoveryserver.jar"]