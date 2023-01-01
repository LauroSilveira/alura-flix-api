package com.alura.aluraflixapi.service;

import com.alura.aluraflixapi.dto.VideoDto;
import com.alura.aluraflixapi.mapper.VideoMapper;
import com.alura.aluraflixapi.model.Video;
import com.alura.aluraflixapi.repository.VideoRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class VideoService {

  private final VideoRepository repository;
  private final VideoMapper mapper;

  @Autowired
  public VideoService(VideoRepository repository, VideoMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public Page<VideoDto> getVideos(Pageable pageable) {
    return repository.findAll(pageable)
        .map(mapper::mapToVideoDto);
  }

  @Transactional
  public VideoDto save(VideoDto dto) {
    try {
      final Video entity = mapper.mapToModel(dto);
      final Video newEntity = repository.save(entity);
      return mapper.mapToVideoDto(newEntity);
    } catch (Exception e) {
      throw new RuntimeException("Error to persist entity", e.getCause());
    }
  }

  @Transactional
  public VideoDto updatePut(VideoDto dto) {
    try {
      final Video entity = mapper.mapToModel(dto);
      repository.save(entity);
      return dto;
    } catch (Exception e) {
      log.error("Error to try Update entity by method Put {}", e.getMessage());
      return null;
    }

  }

  @Transactional
  public Optional<VideoDto> delete(String id) {
    Optional<Video> entityToDelete = repository.findById(id);
    entityToDelete.ifPresent(repository::delete);
    return entityToDelete.map(mapper::mapToVideoDto);
  }

  public VideoDto getById(String id) {
    return repository.findById(id)
        .map(mapper::mapToVideoDto)
        .orElse(null);
  }
}
