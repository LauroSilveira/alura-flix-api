package com.alura.aluraflixapi.infraestructure.service;


import com.alura.aluraflixapi.domain.category.Category;
import com.alura.aluraflixapi.domain.category.CategoryEnum;
import com.alura.aluraflixapi.domain.video.Video;
import com.alura.aluraflixapi.domain.video.dto.UpdateVideoDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import com.alura.aluraflixapi.infraestructure.mapper.VideoMapper;
import com.alura.aluraflixapi.infraestructure.repository.CategoryRepository;
import com.alura.aluraflixapi.infraestructure.repository.VideoRepository;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

  private final VideoRepository videoRepository;
  private final CategoryRepository categoryRepository;
  private final VideoMapper mapper;

  @Autowired
  public VideoServiceImpl(VideoRepository videoRepository, VideoMapper mapper, CategoryRepository categoryRepository) {
    this.videoRepository = videoRepository;
    this.mapper = mapper;
    this.categoryRepository = categoryRepository;
  }

  @Override
  public Page<VideoDto> getVideos(Pageable pageable) {
    Page<Video> videos = videoRepository.findAll(pageable);

    videos.get()
        .forEach(video -> {
          if (Objects.isNull(video.getCategory())) {
            video.setCategory(Category.builder()
                .id(null)
                .rating(CategoryEnum.FREE.name())
                .title(null)
                .colorHex(null)
                .build());
          }
        });
    return videos.map(mapper::mapToVideoDto);
  }

  @Override
  public VideoDto save(VideoDto dto) {
    try {
      final var entityToPersist = mapper.mapToModel(dto);
      final var entityPersisted = this.videoRepository.save(entityToPersist);
      return mapper.mapToVideoDto(entityPersisted);
    } catch (Exception e) {
      throw new RuntimeException("Error to persist entity", e.getCause());
    }
  }

  @Override
  public UpdateVideoDto updateMovie(UpdateVideoDto dto) {
    try {

      final Video entity = mapper.mapUpdateVideoToModel(dto);
      //The mapping framework does not handle cascading saves.
      final var categorySaved = categoryRepository.save(entity.getCategory());
      entity.setCategory(categorySaved);
      videoRepository.save(entity);
      return mapper.mapUpdateVideoDto(entity);
    } catch (Exception e) {
      log.error("Error to try Update entity by method Put {}", e.getMessage());
      return null;
    }

  }

  @Override
  public Optional<VideoDto> delete(String id) {
    Optional<Video> entityToDelete = videoRepository.findById(id);
    entityToDelete.ifPresent(videoRepository::delete);
    return entityToDelete.map(mapper::mapToVideoDto);
  }

  @Override
  public VideoDto getById(String id) {
    return videoRepository.findById(id)
        .map(mapper::mapToVideoDto)
        .orElse(null);
  }
}
