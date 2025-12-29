FROM maven:3.9-amazoncorretto-25-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jdk
WORKDIR /alura-flix-api
ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} alura-flix-api.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "alura-flix-api.jar"]