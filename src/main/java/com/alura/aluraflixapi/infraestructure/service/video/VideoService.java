package com.alura.aluraflixapi.infraestructure.service.video;

import com.alura.aluraflixapi.domain.video.dto.UpdateVideoDTO;
import com.alura.aluraflixapi.domain.video.dto.VideoDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VideoService {

  Page<VideoDTO> getVideos(Pageable pageable);

  VideoDTO save(VideoDTO dto);

  UpdateVideoDTO updateMovie(UpdateVideoDTO dto);

  VideoDTO delete(String id);

  VideoDTO getById(String id);

  List<VideoDTO> getVideosByTitle(String name);
}
