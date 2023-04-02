package com.alura.aluraflixapi.infraestructure.mapper;

import com.alura.aluraflixapi.domain.category.Category;
import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  Category mapperToEntity(CategoryDto categoryDto);

  CategoryDto mapperToCategoryDto(Category categorySaved);
}
