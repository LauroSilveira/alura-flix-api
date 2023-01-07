package com.alura.aluraflixapi.infraestructure.mapper;

import com.alura.aluraflixapi.infraestructure.dto.UpdateVideoDto;
import com.alura.aluraflixapi.infraestructure.dto.VideoDto;
import com.alura.aluraflixapi.domain.Video;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VideoMapper {

  Video mapToModel(VideoDto dto);

  VideoDto mapToVideoDto(Video video);

  UpdateVideoDto mapUpdateVideoDto(Video updateDto);

  Video mapUpdateVideoToModel(UpdateVideoDto updateVideoDto);
}
