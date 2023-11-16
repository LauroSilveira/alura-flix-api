FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
## Arguments used with github secrets
ENV DATABASE_PRO=${DATABASE_PRO}
ENV DATABASE_TEST=${DATABASE_TEST}
ENV JWT_SECRET=${JWT_SECRET}

## work directory by default
WORKDIR /alura-flix-api
COPY --from=build target/*.jar alura-flix-api.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "alura-flix-api.jar"]