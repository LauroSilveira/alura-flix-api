package com.alura.aluraflixapi.infraestructure.railway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RailwayConfig {

    @Bean
    String getPort(){
        return System.getenv("PORT");
    }
}
