package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import com.alura.aluraflixapi.infraestructure.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/category")
@SecurityRequirement(name = "bearer-key")
public class CategoryController {
  private static final String PREFIX_LOGGING = "CategoryController";

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping(produces = "application/json")
  public ResponseEntity<List<CategoryDto>> categories() {
    log.info("{} Request to retrieve all Categories", PREFIX_LOGGING);
    List<CategoryDto> categories = categoryService.categories();
    return ResponseEntity.ok(categories);
  }

  @PostMapping(produces = "application/json", consumes = "application/json")
  public ResponseEntity<CategoryDto> create(@RequestBody @Valid final CategoryDto categoryDto) {
    categoryService.create(categoryDto);
    log.info("{} Request to create a new Category", PREFIX_LOGGING);
    return ResponseEntity.ok(categoryDto);
  }

  @GetMapping(value = "/{id}", produces = "application/json")
  public ResponseEntity<CategoryDto> findCategoryById(@NotBlank @PathVariable final String id) {
    log.info("{} Request to get a Category with Id: {}", PREFIX_LOGGING, id);
    final var category = this.categoryService.findCategoryById(id);
    return ResponseEntity.status(HttpStatus.FOUND).body(category);

  }

  @GetMapping(value = "/{rating}/videos", produces = "application/json")
  public ResponseEntity<List<VideoDto>> getVideosByCategory(
      @NotBlank @PathVariable final String rating) {
    log.info("{} Request to search a Video by Category with Id: {}", PREFIX_LOGGING, rating);
    final var response = this.categoryService.getVideosByCategory(rating);
    if (response.isEmpty()) {
      log.info("{} Response: {}", PREFIX_LOGGING, response);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }
  }

  @DeleteMapping(value = "/{id}", produces = "application/json")
  @PreAuthorize("hasRole('Admin')")
  public ResponseEntity<HttpStatus> deleteCategory(@NotBlank @PathVariable final String id) {
    log.info("{} Request to Delete Category with Id: {}", PREFIX_LOGGING, id);
     this.categoryService.deleteCategory(id);
    return ResponseEntity.ok().build();
  }

}
