# alura-flix-api

[![CI/CD Workflow](https://github.com/LauroSilveira/alura-flix-api/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/LauroSilveira/alura-flix-api/actions/workflows/ci.yml)

This is an application with aim to learning about new features of Spring security from Spring version 6.

## About this project
* [About](#about)
* [Technologies](#Technologies)
* [Generate Swagger with openApi]()
* [How to run](#how-to-run)
  * [Run with Docker](#run-with-docker-compose)
  * [Run with mvn spring-boot:run]()
* [Authentication](#login)
* [Contributors](#contributors)

# About 
This is an application with aim to learning about new features of Spring security from Spring version 6.
The username for test is: guest@aluraflix.com and the password is: 123456, please notice this user has ROLE_USER.

![alura-flix-api-swagger.png](data/alura-flix-api-swagger.png)

# Technologies
- Java 17
- Spring boot 3
- Spring 6
- MongoDB Atlas
- Lombok
- Spring Security
- JWT Token library
- Docker 
- Junit, Mockito
- Jacoco Report

# Generate swagger with openApi
<p>In order to generate swagger openApi without run the application, you can execute</p>

```shell
mvn verify
```
<p>This will generate the swaager on directory /src/main/resources/swagger</p>

# How to Run
### Run with docker-compose
#### Before run with docker-compose - Necessary set up environments
<p>There's two environments variables tha should be setup:</p>

```yaml
- JWT_SECRET=${JWT_SECRET}
- DATABASE_PRO=${DATABASE_PRO}
```
<p>After that it is possible to run just executing docker-compose.yml this will download the latest version</p>

```shell
docker-compose up
```

### Run with mvn spring-boot
```shell
mvn spring-boot:run
```
#### Login
<p>In the authentication-controller use the credentials</p>
<p>username: guest@aluraflix.com / password: 123456 to log in.</p>

![login.png](data/login.png)

<p>After that copy the Token JWT</p>

![token-jwt.png](data/token-jwt.png)

<p>And paste it in the Authorize button</p>

![authorize-token.png](data/authorize-token.png)

That's it, you are already logged and can try out all endpoints :wink:

## Contributors
[@LauroSilveira](https://github.com/LauroSilveira)

<p>Fell free to fork and contribute :wink:</p>
