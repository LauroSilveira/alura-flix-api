# alura-flix-api

This is an application with aim to learning about new features of Spring security from Spring version 6.

# How to Run
If you want to run in command line just go to the same directory of `Application.java` and run:

```shell
mvn spring-boot:run
```

## About this project
* [Summary](#summary)
* [About](#about)
* [Techinologies](#techinologies)
* [How to run](#how-to-run)
* [Requests](#requests)
    * [Authentication](#login)
* [Contributors](#contributors)

# About 
This is an application with aim to learning about new features of Spring security from Spring version 6.
It is possible toa access swagger documentation throught url: http://localhost:8080/swagger-ui/index.html#
The username for test is: guest@aluraflix.com and the password is: 123456, this user has ROLE_USER.

![alura-flix-api-swagger.png](data%2Falura-flix-api-swagger.png)

# Techinologies
- Java 17
- Lombok
- Jackson Faster
- Spring boot 3.0.5
- Spring 6
- Spring Secuity
- JWT Token library

# Requests

#### Login
In the authentication-controller you can use de username: guest@aluraflix.com and the password: 123456 to log in.
![login.png](data%2Flogin.png)

After that copy the Token JWT 
![token-jwt.png](data%2Ftoken-jwt.png)

And paste it in the Authorize button
![authorize-token.png](data%2Fauthorize-token.png)

That's it, you are already logged and can try out all endpoints :wink:

## Contributors
[@LauroSilveira](https://github.com/LauroSilveira)

Fell free to fork and contribute :wink:
