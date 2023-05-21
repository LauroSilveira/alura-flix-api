package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.domain.category.Category;
import com.alura.aluraflixapi.domain.category.Rating;
import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import com.alura.aluraflixapi.domain.video.Video;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import com.alura.aluraflixapi.infraestructure.mapper.CategoryMapperImpl;
import com.alura.aluraflixapi.infraestructure.mapper.VideoMapperImpl;
import com.alura.aluraflixapi.infraestructure.repository.CategoryRepository;
import com.alura.aluraflixapi.infraestructure.repository.VideoRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class CategoryServiceImplTest {

  @SpyBean
  private CategoryServiceImpl categoryService;

  @MockBean
  private CategoryRepository categoryRepository;

  @MockBean
  private VideoRepository videoRepository;

  @SpyBean
  private VideoMapperImpl videoMapper;

  @SpyBean
  private CategoryMapperImpl categoryMapper;

  @Test
  void should_return_all_categories_in_database() {
    //Given
    final var mockCategories = List.of(
        this.buildCategory(UUID.randomUUID().toString(), Rating.FANTASY.getTitle(),
            Rating.FANTASY.getHexDecimalColor(),
            Rating.FANTASY),
        this.buildCategory(UUID.randomUUID().toString(), Rating.ACTION.getTitle(),
            Rating.ACTION.getHexDecimalColor(),
            Rating.ACTION),
        this.buildCategory(UUID.randomUUID().toString(), Rating.TERROR.getTitle(),
            Rating.TERROR.getHexDecimalColor(),
            Rating.TERROR),
        this.buildCategory(UUID.randomUUID().toString(), Rating.TRILLER.getTitle(),
            Rating.TRILLER.getHexDecimalColor(),
            Rating.TRILLER),
        this.buildCategory(UUID.randomUUID().toString(), Rating.ROMANTIC_COMEDY.getTitle(),
            Rating.ROMANTIC_COMEDY.getHexDecimalColor(), Rating.ROMANTIC_COMEDY)
    );

    Mockito.when(this.categoryRepository.findAll()).thenReturn(mockCategories);

    //When
    final var categoryRetrieved = this.categoryService.categories();

    //Then
    Assertions.assertThat(mockCategories).usingRecursiveFieldByFieldElementComparator()
        .isEqualTo(categoryRetrieved);
  }

  @Test
  void should_create_a_new_category_test() {
    //Given
    String id = UUID.randomUUID().toString();
    final var newCategoryEntity = this.buildCategory(id,
        Rating.FANTASY.getTitle(),
        Rating.FANTASY.getHexDecimalColor(),
        Rating.FANTASY);

    Mockito.when(this.categoryRepository.save(Mockito.any()))
        .thenReturn(newCategoryEntity);
    //When
    final var categoryDto = this.categoryService.create(this.buildCategoryDto(id,
        Rating.FANTASY.getTitle(),
        Rating.FANTASY.getHexDecimalColor(),
        Rating.FANTASY));

    //When
    Assertions.assertThat(categoryDto).isNotNull().hasNoNullFieldsOrProperties();
  }

  @Test
  void should_findCategory_by_id_test() {

    //Given
    final var id = UUID.randomUUID().toString();
    final var catgorySearchedById = this.buildCategory(id,
        Rating.ACTION.getTitle(),
        Rating.ACTION.getHexDecimalColor(),
        Rating.ACTION);

    Mockito.when(this.categoryRepository.findById(Mockito.anyString()))
        .thenReturn(Optional.of(catgorySearchedById));
    //When
    final var categoryFound = this.categoryService.findCategoryById(id);

    //When
    Assertions.assertThat(categoryFound).isNotNull();
    Assertions.assertThat(categoryFound.id()).isEqualTo(catgorySearchedById.getId());
    Assertions.assertThat(categoryFound).hasNoNullFieldsOrProperties();

  }

  @Test
  void should_delete_a_category_test() {

    //Given
    final var categoryToBeDeleted = this.buildCategory(UUID.randomUUID().toString(),
        Rating.ACTION.getTitle(),
        Rating.ACTION.getHexDecimalColor(),
        Rating.ACTION);

    //When
    final var httpstatus = this.categoryService.deleteCategory(categoryToBeDeleted.getId());

    //When
    Assertions.assertThat(httpstatus)
        .isNotNull()
        .isEqualTo(HttpStatus.ACCEPTED);
  }

  @Test
  void should_return_a_category_searching_by_video_test() {

    //Given
    final var categorEntity = this.buildCategory(UUID.randomUUID().toString(),
        Rating.ACTION.name(), Rating.ACTION.getHexDecimalColor(), Rating.ACTION);

    Mockito.when(this.categoryRepository.findCategoryByRating(Mockito.anyString()))
        .thenReturn(categorEntity);

    Mockito.when(this.videoRepository.findVideosByCategories(Mockito.anyString()))
        .thenReturn(List.of(getVideoEntity(UUID.randomUUID().toString(),
            "Film Lord of the rings", "www.lordoftherings.com",
            "Lord of the Rings: Fellowship of the ring", categorEntity)));

    //When
    List<VideoDto> videosByCategory = this.categoryService.getVideosByCategory(
        Rating.TRILLER.getTitle());

    //When
    Assertions.assertThat(videosByCategory).isNotNull().isNotEmpty();
    Assertions.assertThat(videosByCategory.stream().findFirst().get().category()).isNotNull();

  }

  private Category buildCategory(final String id, final String title, String colorHex,
      final Rating rating) {
    return Category.builder()
        .id(id)
        .title(title)
        .colorHex(colorHex)
        .rating(rating.name())
        .build();
  }

  private CategoryDto buildCategoryDto(final String id, final String title, String colorHex,
      final Rating rating) {
    return new CategoryDto(id, rating.name(), title, colorHex);
  }

  private Video getVideoEntity(final String id, final String description, final String url,
      final String title, final Category category) {
    return Video.builder()
        .id(id)
        .description(description)
        .url(url)
        .title(title)
        .category(category)
        .build();
  }
}