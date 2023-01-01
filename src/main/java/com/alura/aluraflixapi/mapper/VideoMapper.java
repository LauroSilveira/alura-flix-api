package com.alura.aluraflixapi.mapper;

import com.alura.aluraflixapi.dto.VideoDto;
import com.alura.aluraflixapi.model.Video;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper(componentModel = "spring")
public interface VideoMapper {

  Video mapToModel(VideoDto dto);

  VideoDto mapToVideoDto(Video video);
}
