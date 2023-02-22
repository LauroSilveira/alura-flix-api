package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import java.util.List;
import org.springframework.http.HttpStatus;

public interface CategoryService {

  List<CategoryDto> categories();

  void create(CategoryDto categoryDto);

  CategoryDto findCategoryById(String id);

  HttpStatus deleteCategory(String id);

  List<VideoDto> getVideosByCategory(String id);
}
