package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import com.alura.aluraflixapi.infraestructure.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@SecurityRequirement(name = "bearer-key")
public class CategoryController {


  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public ResponseEntity<List<CategoryDto>> categories() {
    List<CategoryDto> categories = categoryService.categories();
    return ResponseEntity.ok(categories);
  }

  @PostMapping
  public ResponseEntity<CategoryDto> create(@RequestBody @Valid final CategoryDto categoryDto) {
    categoryService.create(categoryDto);
    return ResponseEntity.ok(categoryDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryDto> findCategoryById(@NotBlank @PathVariable final String id) {
    final var category = this.categoryService.findCategoryById(id);
    return ResponseEntity.status(HttpStatus.FOUND).body(category);

  }

  @GetMapping("/{id}/videos")
  public ResponseEntity<List<VideoDto>> getVideosByCategory(
      @NotBlank @PathVariable final String id) {
    final var videosByCategory = this.categoryService.getVideosByCategory(id);
    if (videosByCategory.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.FOUND).body(videosByCategory);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteCategory(@NotBlank @PathVariable final String id) {
    final var response = this.categoryService.deleteCategory(id);
    return ResponseEntity.status(response).build();
  }

}
