package com.alura.aluraflixapi.config.security;

import com.alura.aluraflixapi.model.RoleEnum;
import com.alura.aluraflixapi.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

/*  @Bean
  public AuthenticationManager authManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder managerBuilder = http.getSharedObject(
        AuthenticationManagerBuilder.class);
    managerBuilder.userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
    return managerBuilder.build();
  }*/


  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    return http.httpBasic()
        .and()
        .authorizeHttpRequests()
/*        .antMatchers(HttpMethod.GET, "/videos/**").permitAll()
        .antMatchers(HttpMethod.POST, "/user/**").hasRole(RoleEnum.ADMIN.name())
        .antMatchers(HttpMethod.POST, "/videos/**").hasRole(RoleEnum.ADMIN.name())
        .antMatchers(HttpMethod.DELETE, "/videos/**").hasRole(RoleEnum.ADMIN.name())*/
        .anyRequest()
        .authenticated()
        .and()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().build();
  }

}
