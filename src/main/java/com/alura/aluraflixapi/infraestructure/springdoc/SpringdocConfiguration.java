package com.alura.aluraflixapi.infraestructure.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringdocConfiguration {
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(new Components()
            .addSecuritySchemes("bearer-key",
                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
        .info(new Info().title("alura-flix-api")
            .description("An API Rest about movies and series that implements Get, Post, Put and Delet methods."
                + " Also implements JWT security with Spring Framework")
            .contact(new Contact()
                .name("Lauro Correia")
                .email("lauro.silveira@ymail.com"))
            .license(new License()
                .name("Apache 2.0")
                .url("http://voll.med/api/licenca")
            )
        );
  }

}
