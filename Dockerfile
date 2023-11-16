FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
COPY . .
RUN mvn clean package 

FROM eclipse-temurin:21-jre-alpine
## Arguments used with github secrets
ARG DATABASE_PRO
ARG DATABASE_TEST
ARG JWT_SECRET

## work directory by default
WORKDIR /alura-flix-api
COPY --from=build target/*.jar alura-flix-api.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "alura-flix-api.jar"]