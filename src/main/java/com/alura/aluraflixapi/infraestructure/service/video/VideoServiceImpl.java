package com.alura.aluraflixapi.infraestructure.service.video;


import com.alura.aluraflixapi.domain.category.Category;
import com.alura.aluraflixapi.domain.category.Rating;
import com.alura.aluraflixapi.domain.video.Video;
import com.alura.aluraflixapi.domain.video.dto.UpdateVideoDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import com.alura.aluraflixapi.infraestructure.exception.ResourceNotFoundException;
import com.alura.aluraflixapi.infraestructure.exception.VideoServiceException;
import com.alura.aluraflixapi.infraestructure.mapper.VideoMapper;
import com.alura.aluraflixapi.infraestructure.repository.CategoryRepository;
import com.alura.aluraflixapi.infraestructure.repository.VideoRepository;

import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private static final String LOGGING_PREFIX = "[VideoServiceImpl]";
    private final VideoRepository videoRepository;
    private final CategoryRepository categoryRepository;
    private final VideoMapper videoMapper;

    @Override
    public Page<VideoDto> getVideos(Pageable pageable) {
        Page<Video> pages = videoRepository.findAll(pageable);

        pages.get()
                .forEach(video -> {
                    if (Objects.isNull(video.getCategory())) {
                        video.setCategory(Category.builder()
                                .id("")
                                .rating(Rating.FREE.name())
                                .title("")
                                .colorHex("")
                                .build());
                    }
                });

        return new PageImpl<>(this.videoMapper.maptoList(pages.getContent()));
    }

    @Override
    public VideoDto save(VideoDto dto) {
        try {
            log.info("{} Saving new video: {}", LOGGING_PREFIX, dto.toString());
            final var entityToPersist = videoMapper.mapToModel(dto);
            final var entityPersisted = this.videoRepository.save(entityToPersist);
            log.info("{} New Video saved", LOGGING_PREFIX);
            return videoMapper.mapToVideoDto(entityPersisted);
        } catch (Exception e) {
            throw new VideoServiceException("Error to persist entity", e.getCause());
        }
    }

    @Override
    public UpdateVideoDto updateMovie(UpdateVideoDto dto) {
        try {

            final Video entity = videoMapper.mapToModel(dto);
            //The mapping framework does not handle cascading saves.
            final var categorySaved = categoryRepository.save(entity.getCategory());
            entity.setCategory(categorySaved);
            videoRepository.save(entity);
            return videoMapper.mapToUpdateVideoDto(entity);
        } catch (Exception e) {
            throw new VideoServiceException("Error to update movie", e.getCause());
        }

    }

    @Override
    public VideoDto delete(String id) {
        final var entity = videoRepository.findById(id);
        if (entity.isPresent()) {
            this.videoRepository.delete(entity.get());
            return this.videoMapper.mapToVideoDto(entity.get());
        } else {
            throw new ResourceNotFoundException("Resource not found: " + id);
        }
    }

    @Override
    public VideoDto getById(String id) {
        return videoRepository.findById(id)
                .map(videoMapper::mapToVideoDto)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found for id: " + id));
    }

    @Override
    public List<VideoDto> getVideosByTitle(String name) {
        final var videos = videoRepository.findByTitleLike(name)
                .stream()
                .map(this.videoMapper::mapToVideoDto)
                .toList();
        if (videos.isEmpty()) {
            throw new ResourceNotFoundException("Video not found with title: " + name);
        } else {
            return videos;
        }
    }
}
