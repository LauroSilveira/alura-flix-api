package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.domain.category.Category;
import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import com.alura.aluraflixapi.infraestructure.exception.CategoryTransactionException;
import com.alura.aluraflixapi.infraestructure.mapper.VideoMapper;
import com.alura.aluraflixapi.infraestructure.repository.CategoryRepository;
import com.alura.aluraflixapi.infraestructure.repository.VideoRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CategoryImpl implements CategoryService {


  private final CategoryRepository categoryRepository;

  private final VideoRepository videoRepository;

  private final VideoMapper videoMapper;


  public CategoryImpl(CategoryRepository categoryRepository, VideoRepository videoRepository,
      VideoMapper videoMapper) {
    this.categoryRepository = categoryRepository;
    this.videoRepository = videoRepository;
    this.videoMapper = videoMapper;
  }

  @Override
  public List<CategoryDto> categories() {
    return categoryRepository.findAll()
        .stream()
        .map(entity -> new CategoryDto(entity.getId(), entity.getRating(), entity.getTitle(),
            entity.getColorHex()))
        .toList();
  }

  @Override
  @Transactional
  public void create(CategoryDto categoryDto) {
    try {
      final var entity = new Category(categoryDto.id(), categoryDto.rating(), categoryDto.title(),
          categoryDto.colorHex());
      categoryRepository.save(entity);
    } catch (MongoTransactionException exception) {
      throw new CategoryTransactionException("Error to persist new category",
          exception.getMostSpecificCause());
    }
  }

  @Override
  public CategoryDto findCategoryById(String id) {
    return this.categoryRepository.findById(id)
        .map(
            document -> new CategoryDto(document.getId(), document.getRating(), document.getTitle(),
                document.getColorHex()))
        .orElse(null);
  }

  @Override
  @Transactional
  public HttpStatus deleteCategory(String id) {
    try {
      this.categoryRepository.deleteById(id);
      return HttpStatus.ACCEPTED;
    } catch (MongoTransactionException exception) {
      log.error("Error", exception.getCause());
      return HttpStatus.NO_CONTENT;
    }
  }

  @Override
  public List<VideoDto> getVideosByCategory(String rating) {
    final var category = categoryRepository.findCategoryByRating(rating);
    return videoRepository.findVideosByCategories(category.getId())
        .stream().map(this.videoMapper::mapToVideoDto)
        .toList();
  }
}
