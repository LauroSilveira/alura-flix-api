package com.alura.aluraflixapi.infraestructure.mapper;

import com.alura.aluraflixapi.domain.video.Video;
import com.alura.aluraflixapi.domain.video.dto.UpdateVideoDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import org.mapstruct.Mapper;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VideoMapper {

    Video mapToModel(VideoDto dto);

    VideoDto mapToVideoDto(Video video);

    UpdateVideoDto mapUpdateVideoDto(Video updateDto);

    Video mapUpdateVideoToModel(UpdateVideoDto updateVideoDto);

    List<VideoDto> maptoList(List<Video> videos);
}
