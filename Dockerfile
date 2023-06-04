FROM eclipse-temurin:20-jre-alpine

## Arguments used with github secrets
ARG JWT_SECRET
ARG DATABASE_PRO
ARG DATABASE_TEST

## work directory by default
WORKDIR /alura-flix-api
## copy all files from target and put in to work directory
COPY /target/alura-flix-api-0.0.1-SNAPSHOT.jar alura-flix-api.jar
ENTRYPOINT ["java", "-jar", "alura-flix-api.jar"]