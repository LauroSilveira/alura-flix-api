package com.alura.aluraflixapi.infraestructure.railway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RailwayConfig {

    @Value("server.port")
    private String port;
    @Bean
    String getPort(){
        return this.port = System.getenv("PORT");
    }
}
