package com.alura.aluraflixapi.infraestructure.service;


import com.alura.aluraflixapi.domain.category.Category;
import com.alura.aluraflixapi.domain.category.Rating;
import com.alura.aluraflixapi.domain.video.Video;
import com.alura.aluraflixapi.domain.video.dto.UpdateVideoDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import com.alura.aluraflixapi.infraestructure.mapper.VideoMapper;
import com.alura.aluraflixapi.infraestructure.repository.CategoryRepository;
import com.alura.aluraflixapi.infraestructure.repository.VideoRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

  private static final String LOGGIN_PREFIX = "[VideoServiceImpl]";
  private final VideoRepository videoRepository;
  private final CategoryRepository categoryRepository;
  private final VideoMapper videoMapper;

  @Autowired
  public VideoServiceImpl(VideoRepository videoRepository, VideoMapper videoMapper,
      CategoryRepository categoryRepository) {
    this.videoRepository = videoRepository;
    this.videoMapper = videoMapper;
    this.categoryRepository = categoryRepository;
  }

  @Override
  public Page<VideoDto> getVideos(Pageable pageable) {
    Page<Video> pages = videoRepository.findAll(pageable);

    pages.get()
        .forEach(video -> {
          if (Objects.isNull(video.getCategory())) {
            video.setCategory(Category.builder()
                .id(null)
                .rating(Rating.FREE.name())
                .title(null)
                .colorHex(null)
                .build());
          }
        });

    return new PageImpl<>(this.videoMapper.maptoList(pages.getContent()));
  }

  @Override
  public VideoDto save(VideoDto dto) {
    try {
      log.info("{} Saving new video: {}", LOGGIN_PREFIX, dto.toString());
      final var entityToPersist = videoMapper.mapToModel(dto);
      final var entityPersisted = this.videoRepository.save(entityToPersist);
      log.info("{} New Video saved", LOGGIN_PREFIX);
      return videoMapper.mapToVideoDto(entityPersisted);
    } catch (Exception e) {
      throw new RuntimeException("Error to persist entity", e.getCause());
    }
  }

  @Override
  public UpdateVideoDto updateMovie(UpdateVideoDto dto) {
    try {

      final Video entity = videoMapper.mapUpdateVideoToModel(dto);
      //The mapping framework does not handle cascading saves.
      final var categorySaved = categoryRepository.save(entity.getCategory());
      entity.setCategory(categorySaved);
      videoRepository.save(entity);
      return videoMapper.mapUpdateVideoDto(entity);
    } catch (Exception e) {
      log.error("Error to try Update entity by method Put {}", e.getMessage());
      return null;
    }

  }

  @Override
  public Optional<VideoDto> delete(String id) {
    Optional<Video> entityToDelete = videoRepository.findById(id);
    entityToDelete.ifPresent(videoRepository::delete);
    return entityToDelete.map(videoMapper::mapToVideoDto);
  }

  @Override
  public VideoDto getById(String id) {
    return videoRepository.findById(id)
        .map(videoMapper::mapToVideoDto)
        .orElse(null);
  }

  @Override
  public List<VideoDto> getVideosByTitle(String name) {
    return this.videoRepository.findByTitleLike(name)
        .stream()
        .map(this.videoMapper::mapToVideoDto)
        .toList();
  }
}
