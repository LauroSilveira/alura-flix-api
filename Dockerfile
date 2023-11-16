FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /alura-flix-api
ARG DATABASE_PRO=mongodb+srv://alura-flix-admin:UxOM6SSAtQHpDeEd@alura-flix.z61opfc.mongodb.net/?retryWrites=true&w=majority
ARG DATABASE_TEST=mongodb+srv://laurosilveira:6Vos5cYJ9JfxidsQ@alura-flix-test.qixe2kd.mongodb.net/?retryWrites=true&w=majority
ARG JWT_SECRET=123456
COPY --from=build target/*.jar alura-flix-api.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "alura-flix-api.jar"]