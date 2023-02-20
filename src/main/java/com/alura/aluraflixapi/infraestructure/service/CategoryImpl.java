package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.domain.category.Category;
import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import com.alura.aluraflixapi.infraestructure.exception.CategoryTransactionException;
import com.alura.aluraflixapi.infraestructure.repository.CategoryRepository;
import java.util.List;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryImpl implements CategoryService {


  private final CategoryRepository categoryRepository;

  public CategoryImpl(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public List<CategoryDto> getCategories() {
    return categoryRepository.findAll()
        .stream()
        .map(entity -> new CategoryDto(entity.getId(), entity.getTitle(), entity.getColorHex()))
        .toList();
  }

  @Override
  @Transactional
  public void create(CategoryDto categoryDto) {
    try {
      final var entity = new Category(categoryDto.id(), categoryDto.title(),
          categoryDto.colorHex());
      categoryRepository.save(entity);
    } catch (MongoTransactionException exception) {
      throw  new CategoryTransactionException("Error to persist new category", exception.getMostSpecificCause());
    }
  }
}
