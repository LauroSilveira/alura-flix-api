package com.alura.aluraflixapi.controller.category;

import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDTO;
import com.alura.aluraflixapi.infraestructure.service.category.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class CategoryController {
    private static final String PREFIX_LOGGING = "CategoryController";

    private final CategoryService categoryService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<CategoryDto>> categories() {
        List<CategoryDto> categories = categoryService.categories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid final CategoryDto categoryDto) {
        categoryService.create(categoryDto);
        return ResponseEntity.ok(categoryDto);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<CategoryDto> findCategoryById(@NotBlank @PathVariable final String id) {
        final var category = this.categoryService.findCategoryById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(category);

    }

    @GetMapping(value = "/{rating}/videos", produces = "application/json")
    public ResponseEntity<List<VideoDTO>> getVideosByCategory(
            @NotBlank @PathVariable final String rating) {
        log.info("{} Request to search a Video by Category with Id: {}", PREFIX_LOGGING, rating);
        final var response = this.categoryService.getVideosByCategory(rating);
        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).body(response);
        }
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<HttpStatus> deleteCategory(@NotBlank @PathVariable final String id) {
        this.categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

}
