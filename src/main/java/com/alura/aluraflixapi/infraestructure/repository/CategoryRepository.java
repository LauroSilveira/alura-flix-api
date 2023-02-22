package com.alura.aluraflixapi.infraestructure.repository;

import com.alura.aluraflixapi.domain.category.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {

  Category findCategoryByRating(String rating);
}
