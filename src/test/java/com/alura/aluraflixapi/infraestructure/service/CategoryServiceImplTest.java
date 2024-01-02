package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.domain.category.Category;
import com.alura.aluraflixapi.domain.category.Rating;
import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import com.alura.aluraflixapi.domain.video.Video;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import com.alura.aluraflixapi.infraestructure.exception.CategoryServiceException;
import com.alura.aluraflixapi.infraestructure.mapper.CategoryMapperImpl;
import com.alura.aluraflixapi.infraestructure.mapper.VideoMapperImpl;
import com.alura.aluraflixapi.infraestructure.repository.CategoryRepository;
import com.alura.aluraflixapi.infraestructure.repository.VideoRepository;
import com.alura.aluraflixapi.infraestructure.service.category.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    void should_return_all_categories_in_database_test() {
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

        when(this.categoryRepository.findAll()).thenReturn(mockCategories);

        //When
        final var categoryRetrieved = this.categoryService.categories();

        //Then
        assertThat(categoryRetrieved)
                .isNotNull()
                .hasSize(5);
    }

    @Test
    void should_create_a_new_category_test() {
        //Given
        String id = UUID.randomUUID().toString();
        final var newCategoryEntity = this.buildCategory(id,
                Rating.FANTASY.getTitle(),
                Rating.FANTASY.getHexDecimalColor(),
                Rating.FANTASY);

        when(this.categoryRepository.save(any()))
                .thenReturn(newCategoryEntity);
        //When
        final var categoryDto = this.categoryService.create(this.buildCategoryDto(id,
                Rating.FANTASY.getTitle(),
                Rating.FANTASY.getHexDecimalColor(),
                Rating.FANTASY));

        //When
        assertThat(categoryDto)
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    void should_throw_categoryTransactionException_when_create_a_new_category_test() {
        //Given
        when(this.categoryRepository.save(any())).thenThrow(MongoTransactionException.class);
        //When
        assertThatExceptionOfType(CategoryServiceException.class)
                .isThrownBy(() -> this.categoryService.create(null)
                ).withMessageContaining("Error to persist new category");
    }

    @Test
    void should_findCategory_by_id_test() {

        //Given
        final var id = UUID.randomUUID().toString();
        final var categorySearchedById = this.buildCategory(id,
                Rating.ACTION.getTitle(),
                Rating.ACTION.getHexDecimalColor(),
                Rating.ACTION);

        when(this.categoryRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(categorySearchedById));
        //When
        final var categoryFound = this.categoryService.findCategoryById(id);

        //When
        assertThat(categoryFound).isNotNull();
        assertThat(categoryFound.id()).isEqualTo(categorySearchedById.getId());
        assertThat(categoryFound).hasNoNullFieldsOrProperties();

    }

    @Test
    void should_delete_a_category_test() {

        //Given
        final var category = this.buildCategory(UUID.randomUUID().toString(),
                Rating.ACTION.getTitle(),
                Rating.ACTION.getHexDecimalColor(),
                Rating.ACTION);

        when(this.categoryRepository.findById(any())).thenReturn(Optional.of(category));
        //When
        this.categoryService.deleteCategory(category.getId());

        //Then
        Mockito.verify(this.categoryService, Mockito.atLeast(1)).deleteCategory(category.getId());
    }

    @Test
    void should_return_a_category_searching_by_video_test() {

        //Given
        final var entity = this.buildCategory(UUID.randomUUID().toString(),
                Rating.ACTION.name(), Rating.ACTION.getHexDecimalColor(), Rating.ACTION);

        when(this.categoryRepository.findCategoryByRating(Mockito.anyString()))
                .thenReturn(entity);

        when(this.videoRepository.findVideosByCategories(Mockito.anyString()))
                .thenReturn(List.of(getVideoEntity(UUID.randomUUID().toString(),
                        "Film Lord of the rings", "www.lordoftherings.com",
                        "Lord of the Rings: Fellowship of the ring", entity)));

        //When
        List<VideoDto> videosByCategory = this.categoryService.getVideosByCategory(
                Rating.TRILLER.getTitle());

        //When
        assertThat(videosByCategory).isNotNull().isNotEmpty();
        assertThat(videosByCategory.stream().findFirst().get().category()).isNotNull();

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