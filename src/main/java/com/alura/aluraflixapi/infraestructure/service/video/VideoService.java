package com.alura.aluraflixapi.infraestructure.service.video;

import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import com.alura.aluraflixapi.domain.video.dto.UpdateVideoDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface VideoService {

  Page<VideoDto> getVideos(Pageable pageable);

  VideoDto save(VideoDto dto);

  UpdateVideoDto updateMovie(UpdateVideoDto dto);

  VideoDto delete(String id);

  VideoDto getById(String id);

  List<VideoDto> getVideosByTitle(String name);
}
