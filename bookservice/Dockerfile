FROM maven:3.9.9 AS build
WORKDIR /app
COPY ./bookservice ./bookservice
COPY ./commonservice ./commonservice

WORKDIR /app/commonservice
RUN mvn clean install -DskipTests

WORKDIR /app/bookservice
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/bookservice/target/*.jar bookservice.jar
EXPOSE 8002
ENTRYPOINT ["java", "-jar", "discoveryserver.jar"]