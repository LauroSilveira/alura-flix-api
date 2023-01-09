package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.domain.video.dto.UpdateVideoDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface VideoService {

  Page<VideoDto> getVideos(Pageable pageable);

  @Transactional
  VideoDto save(VideoDto dto);

  @Transactional
  UpdateVideoDto updateMovie(UpdateVideoDto dto);

  @Transactional
  Optional<VideoDto> delete(String id);

  VideoDto getById(String id);
}
