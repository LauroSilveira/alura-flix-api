package com.alura.aluraflixapi.infraestructure.mapper;

import com.alura.aluraflixapi.domain.category.Category;
import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-26T10:55:33+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.8 (Homebrew)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category mapperToEntity(CategoryDto categoryDto) {
        if ( categoryDto == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        category.id( categoryDto.id() );
        category.rating( categoryDto.rating() );
        category.title( categoryDto.title() );
        category.colorHex( categoryDto.colorHex() );

        return category.build();
    }

    @Override
    public CategoryDto mapperToCategoryDto(Category categorySaved) {
        if ( categorySaved == null ) {
            return null;
        }

        String id = null;
        String rating = null;
        String title = null;
        String colorHex = null;

        id = categorySaved.getId();
        rating = categorySaved.getRating();
        title = categorySaved.getTitle();
        colorHex = categorySaved.getColorHex();

        CategoryDto categoryDto = new CategoryDto( id, rating, title, colorHex );

        return categoryDto;
    }
}
