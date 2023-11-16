FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /alura-flix-api
ARG DATABASE_PRO
ARG DATABASE_TEST
ENV JWT_SECRET=123456
COPY --from=build target/*.jar alura-flix-api.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "alura-flix-api.jar"]