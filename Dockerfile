FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
COPY . .
RUN mvn clean package

FROM eclipse-temurin:21-jre-alpine
## Arguments used with github secrets
ARG JWT_SECRET
ARG DATABASE_PRO
ARG DATABASE_TEST

## work directory by default
WORKDIR /alura-flix-api
## copy all files from target and put in to work directory
COPY --from=build target/alura-flix-api-0.0.1-SNAPSHOT.jar alura-flix-api.jar
ENTRYPOINT ["java", "-jar", "alura-flix-api.jar"]