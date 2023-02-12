package com.alura.aluraflixapi.infraestructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main Spring Security class configuration In Spring 3.0 the security configuration is done by
 * @Bean
 */

@Configuration
@EnableWebSecurity
//enable @Secure("Role_XX")
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfigurations {

  @Autowired
  private SecurityFilter securityFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //disabel cross site request forgery
    return http.csrf().disable()
        //Disable Spring controll and expone all endpoints
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().authorizeHttpRequests()
        //exclude only enpoint Login
        .requestMatchers(HttpMethod.POST,"/login").permitAll()
        //any other requeste has to be authenticated
        .anyRequest().authenticated()
        .and()
        //tell to spring to user our filter SecurityFilter.class instead their
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }


  /**
   * Crete @Bean AuthenticationManager to authenticate a user
   * @return AuthenticationManager
   */
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  /**
   * Bean to encript and decript password
   * @return new PasswordEncoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
