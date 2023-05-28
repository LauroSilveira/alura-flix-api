# alura-flix-api

<p>This is an application with aim to learning about new features of Spring security from Spring version 6.<p/>

## About this project
* [About](#about)
* [Techinologies](#techinologies)
* [How to run](#how-to-run)
* [Authentication](#login)
* [Contributors](#contributors)

# About 
<p>This is an application with aim to learning about new features of Spring security from Spring version 6.
It is possible toa access swagger documentation throught url: [http://localhost:8080/swagger](http://localhost:8080/swagger-ui/index.html#)
The username for test is: guest@aluraflix.com and the password is: 123456, this user has ROLE_USER.<p/>

![alura-flix-api-swagger.png](data%2Falura-flix-api-swagger.png)

# Techinologies
- Java 17
- Lombok
- Jackson Faster
- Spring boot 3.0.5
- Spring 6
- Spring Secuity
- JWT Token library

# How to Run
<p>If you want to run in command line just go to the same directory of `Application.java` and run: <p/>

```shell
mvn spring-boot:run
```

#### Login
<p>In the authentication-controller use the credentials<p/>
<p>username: guest@aluraflix.com / password: 123456 to log in.<p/>

![login.png](data%2Flogin.png)

<p>After that copy the Token JWT<p/>

![token-jwt.png](data%2Ftoken-jwt.png)

<p>And paste it in the Authorize button<p/>

![authorize-token.png](data%2Fauthorize-token.png)

That's it, you are already logged and can try out all endpoints :wink:

## Contributors
[@LauroSilveira](https://github.com/LauroSilveira)

<p>Fell free to fork and contribute :wink:<p/>
