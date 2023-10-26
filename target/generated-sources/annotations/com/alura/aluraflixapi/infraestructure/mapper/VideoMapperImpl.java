package com.alura.aluraflixapi.infraestructure.mapper;

import com.alura.aluraflixapi.domain.category.Category;
import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import com.alura.aluraflixapi.domain.video.Video;
import com.alura.aluraflixapi.domain.video.dto.UpdateVideoDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-11T11:12:17+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.4 (Oracle Corporation)"
)
@Component
public class VideoMapperImpl implements VideoMapper {

    @Override
    public Video mapToModel(VideoDto dto) {
        if ( dto == null ) {
            return null;
        }

        Video.VideoBuilder video = Video.builder();

        video.id( dto.id() );
        video.title( dto.title() );
        video.description( dto.description() );
        video.url( dto.url() );
        video.category( categoryDtoToCategory( dto.category() ) );

        return video.build();
    }

    @Override
    public VideoDto mapToVideoDto(Video video) {
        if ( video == null ) {
            return null;
        }

        VideoDto.VideoDtoBuilder videoDto = VideoDto.builder();

        videoDto.id( video.getId() );
        videoDto.title( video.getTitle() );
        videoDto.description( video.getDescription() );
        videoDto.url( video.getUrl() );
        videoDto.category( categoryToCategoryDto( video.getCategory() ) );

        return videoDto.build();
    }

    @Override
    public UpdateVideoDto mapUpdateVideoDto(Video updateDto) {
        if ( updateDto == null ) {
            return null;
        }

        String id = null;
        String title = null;
        String description = null;
        String url = null;
        CategoryDto category = null;

        id = updateDto.getId();
        title = updateDto.getTitle();
        description = updateDto.getDescription();
        url = updateDto.getUrl();
        category = categoryToCategoryDto( updateDto.getCategory() );

        UpdateVideoDto updateVideoDto = new UpdateVideoDto( id, title, description, url, category );

        return updateVideoDto;
    }

    @Override
    public Video mapUpdateVideoToModel(UpdateVideoDto updateVideoDto) {
        if ( updateVideoDto == null ) {
            return null;
        }

        Video.VideoBuilder video = Video.builder();

        video.id( updateVideoDto.id() );
        video.title( updateVideoDto.title() );
        video.description( updateVideoDto.description() );
        video.url( updateVideoDto.url() );
        video.category( categoryDtoToCategory( updateVideoDto.category() ) );

        return video.build();
    }

    protected Category categoryDtoToCategory(CategoryDto categoryDto) {
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

    protected CategoryDto categoryToCategoryDto(Category category) {
        if ( category == null ) {
            return null;
        }

        String id = null;
        String rating = null;
        String title = null;
        String colorHex = null;

        id = category.getId();
        rating = category.getRating();
        title = category.getTitle();
        colorHex = category.getColorHex();

        CategoryDto categoryDto = new CategoryDto( id, rating, title, colorHex );

        return categoryDto;
    }
}
