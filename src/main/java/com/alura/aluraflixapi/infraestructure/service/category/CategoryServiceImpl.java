package com.alura.aluraflixapi.infraestructure.service.category;

import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import com.alura.aluraflixapi.infraestructure.exception.CategoryServiceException;
import com.alura.aluraflixapi.infraestructure.mapper.CategoryMapper;
import com.alura.aluraflixapi.infraestructure.mapper.VideoMapper;
import com.alura.aluraflixapi.infraestructure.repository.CategoryRepository;
import com.alura.aluraflixapi.infraestructure.repository.VideoRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;

    private final VideoRepository videoRepository;

    private final VideoMapper videoMapper;

    private final CategoryMapper categoryMapper;


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
    public CategoryDto create(CategoryDto categoryDto) {
        try {
            final var entity = this.categoryMapper.mapperToEntity(categoryDto);
            final var categorySaved = categoryRepository.save(entity);
            return this.categoryMapper.mapperToCategoryDto(categorySaved);
        } catch (MongoTransactionException exception) {
            throw new CategoryServiceException("Error to persist new category",
                    exception.getMostSpecificCause());
        }
    }

    @Override
    public CategoryDto findCategoryById(String id) {
        return this.categoryRepository.findById(id)
                .map(
                        document -> new CategoryDto(document.getId(), document.getRating(), document.getTitle(),
                                document.getColorHex()))
                .orElseThrow(() -> new CategoryServiceException("Category not found: " + id));
    }

    @Override
    @Transactional
    public void deleteCategory(String id) {

        final var isCategoryPresent = this.categoryRepository.findById(id);
        isCategoryPresent.ifPresentOrElse(this.categoryRepository::delete, () -> {
            throw new CategoryServiceException("Category not found to delete with Id:" + id);
        });

    }

    @Override
    public List<VideoDto> getVideosByCategory(String rating) {
        final var category = categoryRepository.findCategoryByRating(rating);
        return videoRepository.findVideosByCategories(category.getId())
                .stream().map(this.videoMapper::mapToVideoDto)
                .toList();
    }
}
