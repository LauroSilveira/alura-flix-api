package com.alura.aluraflixapi.infraestructure.mapper;

import com.alura.aluraflixapi.domain.video.Video;
import com.alura.aluraflixapi.domain.video.dto.UpdateVideoDTO;
import com.alura.aluraflixapi.domain.video.dto.VideoDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VideoMapper {

    Video mapToModel(VideoDTO dto);

    VideoDTO mapToVideoDto(Video video);

    UpdateVideoDTO mapToUpdateVideoDto(Video model);

    Video mapToModel(UpdateVideoDTO updateVideoDto);

    List<VideoDTO> maptoList(List<Video> videos);
}
