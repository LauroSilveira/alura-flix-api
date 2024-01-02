package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.domain.video.Video;
import com.alura.aluraflixapi.domain.video.dto.UpdateVideoDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import com.alura.aluraflixapi.infraestructure.exception.ResourceNotFoundException;
import com.alura.aluraflixapi.infraestructure.exception.VideoServiceException;
import com.alura.aluraflixapi.infraestructure.mapper.VideoMapperImpl;
import com.alura.aluraflixapi.infraestructure.repository.CategoryRepository;
import com.alura.aluraflixapi.infraestructure.repository.VideoRepository;
import com.alura.aluraflixapi.infraestructure.service.video.VideoServiceImpl;
import com.alura.aluraflixapi.jsonutils.ParseJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class VideoServiceImplTest extends ParseJson {

    private static final String PREFIX_PATH = "/video/";

    @MockBean
    private VideoRepository videoRepository;

    @SpyBean
    private VideoMapperImpl videoMapper;

    @MockBean
    private CategoryRepository categoryRepository;

    @SpyBean
    private VideoServiceImpl videoService;

    @Test
    @DisplayName("Should return all videos")
    void getVideos_response_ok_test() {
        //Given
        final var jsonFile = getJsonFile(PREFIX_PATH + "getAllVideos_response_ok.json");
        final var videos = parseToJavaObject(jsonFile, Video[].class);
        final Page<Video> pagesVideos = new PageImpl<>(Arrays.stream(videos).toList());

        final var videosDto = this.videoMapper.maptoList(Arrays.stream(videos).toList());
        when(this.videoRepository.findAll(any(Pageable.class))).thenReturn(pagesVideos);
        when(this.videoMapper.maptoList(anyList())).thenReturn(videosDto);

        //when
        final var response = this.videoService.getVideos(Pageable.ofSize(10));

        //Then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).usingRecursiveComparison().isEqualTo(videosDto);
    }

    @Test
    @DisplayName("Should return a empty video when getAll is null")
    void getVideos_response_ko_test() {
        //Given
        final var videoWithoutCategory = new Video(null, null, null, null, null);
        final Page<Video> page = new PageImpl<>(List.of(videoWithoutCategory));
        when(this.videoRepository.findAll(any(Pageable.class))).thenReturn(page);

        //when
        final var response = this.videoService.getVideos(Pageable.ofSize(1));

        //Then
        assertThat(response).isNotNull();

    }

    @Test
    @DisplayName("Should save a new video")
    void save_response_ok_test() {
        //Given
        final var jsonFile = getJsonFile(PREFIX_PATH + "create_video_response_ok.json");
        final var videoDto = parseToJavaObject(jsonFile, VideoDto.class);
        final var videoModel = this.videoMapper.mapToModel(videoDto);
        when(this.videoMapper.mapToModel(any(VideoDto.class))).thenReturn(videoModel);
        when(this.videoMapper.mapToVideoDto(any())).thenReturn(videoDto);

        //When
        final var videoCreated = this.videoService.save(videoDto);

        //then
        assertThat(videoCreated).isNotNull();
        assertThat(videoCreated).usingRecursiveComparison().isEqualTo(videoDto);
    }

    @Test
    @DisplayName("Should throw a VideoServiceException when try to save a new video")
    void save_response_ko_test() {
        //When
        assertThatExceptionOfType(VideoServiceException.class)
                .isThrownBy(() -> this.videoService.save(null)
                ).withMessageContaining("Error to persist entity");
    }

    @Test
    @DisplayName("Should receive a VideoUpdateDto and update a video already exist")
    void updateMovie_response_ok_test() {

        //Given
        final var jsonFile = getJsonFile(PREFIX_PATH + "update_video_response_ok.json");
        final var updateVideoDtoParsed = parseToJavaObject(jsonFile, UpdateVideoDto.class);
        final var model = this.videoMapper.mapToModel(updateVideoDtoParsed);
        final var updateVideoDto = this.videoMapper.mapToUpdateVideoDto(model);
        when(this.videoMapper.mapToModel(any(UpdateVideoDto.class))).thenReturn(model);
        when(this.videoMapper.mapToUpdateVideoDto(any())).thenReturn(updateVideoDto);

        //When
        final var response = this.videoService.updateMovie(updateVideoDtoParsed);

        //Then
        assertThat(response).isNotNull();
        assertThat(response).usingRecursiveComparison().isEqualTo(updateVideoDtoParsed);
    }

    @Test
    @DisplayName("Should throw a VideoServiceException when try to update a video")
    void updateVideo_response_ko_test() {
        //When
        assertThatExceptionOfType(VideoServiceException.class)
                .isThrownBy(() -> this.videoService.updateMovie(null)
                ).withMessageContaining("Error to update movie");
    }

    @Test
    @DisplayName("Should delete a video passing a Id")
    void delete_response_ok_test() {
        //Given
        final var jsonFile = getJsonFile(PREFIX_PATH + "delete_video_response_ok.json");
        final var model = parseToJavaObject(jsonFile, Video.class);
        final var videoDto = this.videoMapper.mapToVideoDto(model);
        when(this.videoRepository.findById(anyString())).thenReturn(Optional.of(model));
        when(this.videoMapper.mapToVideoDto(model)).thenReturn(videoDto);

        //When
        final var response = this.videoService.delete("63680c011892283477b3e9b9");

        //Then
        assertThat(response).usingRecursiveComparison().isEqualTo(videoDto);
    }

    @Test
    @DisplayName("Should throw a VideoServiceException when try to update a video")
    void deleteVideo_response_ko_test() {
        //When
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> this.videoService.delete(null)
                ).withMessageContaining("Resource not found:");
    }

    @Test
    @DisplayName("Should return a video filtering by Id")
    void getById_response_ok_test() {
        //Given
        final var jsonFile = getJsonFile(PREFIX_PATH + "getById_video_response_ok.json");
        final var model = parseToJavaObject(jsonFile, Video.class);
        when(videoRepository.findById(anyString())).thenReturn(Optional.of(model));

        //When
        final var response = this.videoService.getById("63680c011892283477b3e9b9");

        //Then
        assertThat(response).usingRecursiveComparison().isEqualTo(this.videoMapper.mapToVideoDto(model));
    }

    @Test
    @DisplayName("Should return a ResourceNotFoundException when search by Id")
    void getById_response_ko_test() {
        //When
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() ->  this.videoService.getById(null))
                .isInstanceOf(ResourceNotFoundException.class)
                .withMessageContaining("Resource not found for id:");
    }

    @Test
    @DisplayName("Should return a List of videos filtering by Video title")
    void getVideosByTitle_response_ok_test() {
        //Given
        final var jsonFile = getJsonFile(PREFIX_PATH + "getById_video_response_ok.json");
        final var video = parseToJavaObject(jsonFile, Video.class);
        final var videoDtoExpected = this.videoMapper.mapToVideoDto(video);
        when(this.videoRepository.findByTitleLike(anyString())).thenReturn(List.of(video));

        //When
        final var response = this.videoService.getVideosByTitle("Fantasy");

        //Then
        assertThat(response.get(0)).usingRecursiveComparison().isEqualTo(videoDtoExpected);
    }

    @Test
    @DisplayName("Should return a ResourceNotFoundException when search by Id")
    void getVideosByTitle_response_ko_test() {
        //When
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() ->  this.videoService.getVideosByTitle(null))
                .isInstanceOf(ResourceNotFoundException.class)
                .withMessageContaining("Video not found with title:");
    }
}