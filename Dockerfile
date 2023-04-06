FROM openjdk:20-ea-17-oraclelinux8
ENV JWT_SECRET="123456"
#work directory by default
WORKDIR /alura-flix-api
#copy all files from target and put in to work directory
COPY target/alura-flix-api-0.0.1-SNAPSHOT.jar alura-flix-api.jar
ENTRYPOINT ["java", "-jar", "alura-flix-api.jar"]