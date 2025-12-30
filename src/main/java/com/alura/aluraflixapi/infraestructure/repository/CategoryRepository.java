package com.alura.aluraflixapi.infraestructure.repository;

import com.alura.aluraflixapi.domain.category.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {

  Optional<Category> findCategoryByRating(String rating);
}
