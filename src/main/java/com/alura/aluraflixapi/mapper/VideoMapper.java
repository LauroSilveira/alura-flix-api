package com.alura.aluraflixapi.mapper;

import com.alura.aluraflixapi.dto.VideoDto;
import com.alura.aluraflixapi.model.Video;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VideoMapper {

  List<VideoDto> mapToListVideosDto(List<Video> videos);

  Video mapToModel(VideoDto dto);

  VideoDto mapToVideoDto(Video video);
}
