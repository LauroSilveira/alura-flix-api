package com.alura.aluraflixapi.infraestructure.repository;

import com.alura.aluraflixapi.domain.roles.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Roles, String> {

}
