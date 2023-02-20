package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import com.alura.aluraflixapi.infraestructure.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {


  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public ResponseEntity<List<CategoryDto>> categories() {
    List<CategoryDto> categories = categoryService.getCategories();
    return ResponseEntity.ok(categories);
  }

  @PostMapping
  public ResponseEntity<CategoryDto> create(@RequestBody @Valid final CategoryDto categoryDto) {
    categoryService.create(categoryDto);
    return ResponseEntity.ok(categoryDto);
  }

}
