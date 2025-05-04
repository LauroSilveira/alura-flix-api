package com.alura.aluraflixapi.infraestructure.security;

import lombok.RequiredArgsConstructor;
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
 *
 * @Bean
 */

@Configuration
//Enable Custom configuration spring boot
@EnableWebSecurity
//enable @Secure("Role_XX")
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //disable Cross Site Request Forgery
        return http.csrf(csrf -> csrf.ignoringRequestMatchers("/login/**"))
                //Configure to be stateless
                .sessionManagement(managementConfigurer ->
                        managementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(httpRequest -> httpRequest
                        .requestMatchers(HttpMethod.POST, "/login/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        //any other request has to be authenticated
                        .anyRequest().authenticated()
                )
                //tell to spring using our filter SecurityFilter.class instead their
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                //tell to spring using this filter to handle any exception about JWT exception
                .exceptionHandling(exceptionHandler ->
                        exceptionHandler.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .build();
    }

    /**
     * Create @Bean AuthenticationManager to authenticate a user
     *
     * @return AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Bean to encrypt and decrypt password
     *
     * @return new PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
