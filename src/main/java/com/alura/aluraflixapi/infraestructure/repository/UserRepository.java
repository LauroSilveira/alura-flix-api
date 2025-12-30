package com.alura.aluraflixapi.infraestructure.repository;

import com.alura.aluraflixapi.domain.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
