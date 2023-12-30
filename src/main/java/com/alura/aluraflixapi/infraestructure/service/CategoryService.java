package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import java.util.List;
import org.springframework.http.HttpStatus;

public interface CategoryService {

  List<CategoryDto> categories();

  CategoryDto create(CategoryDto categoryDto);

  CategoryDto findCategoryById(String id);

  void deleteCategory(String id);

  List<VideoDto> getVideosByCategory(String rating);
}
