FROM maven:3.9.8-amazoncorretto-21 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /alura-flix-api
ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} alura-flix-api.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "alura-flix-api.jar"]