package com.alura.aluraflixapi.infraestructure.repository;

import com.alura.aluraflixapi.domain.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;


public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}
