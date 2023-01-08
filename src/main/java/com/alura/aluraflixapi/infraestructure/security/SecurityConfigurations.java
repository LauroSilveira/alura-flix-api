package com.alura.aluraflixapi.infraestructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Main Spring Security class configuration
 * In Spring 3.0 the security configuration is done by @Bean
 */

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //disabel cross site request forgery
    return http.csrf().disable()
        //Disable Spring controll and expone all endpoints
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .build();
  }

}
