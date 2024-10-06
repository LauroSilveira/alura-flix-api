package com.alura.aluraflixapi.infraestructure.repository;

import com.alura.aluraflixapi.domain.token.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
}
