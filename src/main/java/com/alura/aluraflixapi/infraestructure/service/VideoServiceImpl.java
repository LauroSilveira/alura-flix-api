package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.infraestructure.dto.UpdateVideoDto;
import com.alura.aluraflixapi.infraestructure.dto.VideoDto;
import com.alura.aluraflixapi.infraestructure.mapper.VideoMapper;
import com.alura.aluraflixapi.infraestructure.repository.VideoRepository;
import com.alura.aluraflixapi.domain.video.Video;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

  private final VideoRepository repository;
  private final VideoMapper mapper;

  @Autowired
  public VideoServiceImpl(VideoRepository repository, VideoMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Page<VideoDto> getVideos(Pageable pageable) {
    return repository.findAll(pageable)
        .map(mapper::mapToVideoDto);
  }

  @Override
  public VideoDto save(VideoDto dto) {
    try {
      final var entityToPersist = mapper.mapToModel(dto);
      final var entityPersisted = this.repository.save(entityToPersist);
      return mapper.mapToVideoDto(entityPersisted);
    } catch (Exception e) {
      throw new RuntimeException("Error to persist entity", e.getCause());
    }
  }

  @Override
  public UpdateVideoDto updateMovie(UpdateVideoDto dto) {
    try {
      final Video entity = mapper.mapUpdateVideoToModel(dto);
      repository.save(entity);
      return mapper.mapUpdateVideoDto(entity);
    } catch (Exception e) {
      log.error("Error to try Update entity by method Put {}", e.getMessage());
      return null;
    }

  }

  @Override
  public Optional<VideoDto> delete(String id) {
    Optional<Video> entityToDelete = repository.findById(id);
    entityToDelete.ifPresent(repository::delete);
    return entityToDelete.map(mapper::mapToVideoDto);
  }

  @Override
  public VideoDto getById(String id) {
    return repository.findById(id)
        .map(mapper::mapToVideoDto)
        .orElse(null);
  }
}
