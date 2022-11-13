package com.alura.aluraflixapi.repository;

import com.alura.aluraflixapi.model.User;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByusername(String username);
}
