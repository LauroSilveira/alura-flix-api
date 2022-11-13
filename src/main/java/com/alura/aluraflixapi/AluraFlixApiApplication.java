package com.alura.aluraflixapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AluraFlixApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(AluraFlixApiApplication.class, args);
    System.out.println("Lauro: " + new BCryptPasswordEncoder().encode("Fredy1104"));
    System.out.println("Cristina: " + new BCryptPasswordEncoder().encode("123456"));
    System.out.println("Elisa: " + new BCryptPasswordEncoder().encode("0007"));
    System.out.println("Oscar: " + new BCryptPasswordEncoder().encode("0001"));
  }

}
