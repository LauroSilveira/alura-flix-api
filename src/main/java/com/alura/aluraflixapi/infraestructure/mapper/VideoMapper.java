package com.alura.aluraflixapi.infraestructure.mapper;

import com.alura.aluraflixapi.domain.video.Video;
import com.alura.aluraflixapi.domain.video.dto.UpdateVideoDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VideoMapper {

    Video mapToModel(VideoDto dto);

    VideoDto mapToVideoDto(Video video);

    UpdateVideoDto mapToUpdateVideoDto(Video model);

    Video mapToModel(UpdateVideoDto updateVideoDto);

    List<VideoDto> maptoList(List<Video> videos);
}
