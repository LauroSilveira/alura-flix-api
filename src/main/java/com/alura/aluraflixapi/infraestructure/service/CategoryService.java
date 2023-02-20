package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import java.util.List;

public interface CategoryService {

  List<CategoryDto> getCategories();

  void create(CategoryDto categoryDto);
}
