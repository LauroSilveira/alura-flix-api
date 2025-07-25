package com.alura.aluraflixapi.infraestructure.springdoc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//Necessary because Swagger sends requests in HTTP
@OpenAPIDefinition(servers = @Server(url = "/", description = "Default server"))
public class SpringdocConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        final String description = """  
                         An API Rest about movies and series that implements Get, Post, Put and Delete methods.
                         Also implements JWT security with Spring Framework and use MongoDB Atlas as database
                There is a user of test you can use, username: guest@aluraflix.com, password: 123456 Please, note that some endpoints are security and this use do not have some permissions 
                any problem let me know by my contact. """;

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info()
                        .title("alura-flix-api")
                        .description(description)
                        .contact(new Contact()
                                .name("Lauro Correia")
                                .email("lauro.silveira@ymail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                        )
                );
    }

}
