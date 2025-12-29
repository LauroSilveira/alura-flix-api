package com.alura.aluraflixapi.infraestructure.service.category;

import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDTO;
import java.util.List;

public interface CategoryService {

  List<CategoryDto> categories();

  CategoryDto create(CategoryDto categoryDto);

  CategoryDto findCategoryById(String id);

  void deleteCategory(String id);

  List<VideoDTO> getVideosByCategory(String rating);
}
