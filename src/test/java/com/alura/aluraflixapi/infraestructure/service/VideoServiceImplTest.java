package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.domain.video.Video;
import com.alura.aluraflixapi.domain.video.dto.UpdateVideoDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import com.alura.aluraflixapi.infraestructure.mapper.VideoMapperImpl;
import com.alura.aluraflixapi.infraestructure.repository.CategoryRepository;
import com.alura.aluraflixapi.infraestructure.repository.VideoRepository;
import com.alura.aluraflixapi.jsonutils.ParseJson;
import org.assertj.core.api.Assertions;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class VideoServiceImplTest extends ParseJson {

    @MockBean
    private VideoRepository videoRepository;
    @SpyBean
    private VideoMapperImpl videoMapper;
    @MockBean
    private CategoryRepository categoryRepository;
    @SpyBean
    private VideoServiceImpl videoService;

    @Test
    void getVideos_response_ok_test() {
        //Given
        final var jsonFile = getJsonFile("getAllVideos_response_ok.json");
        final var videos = parseToJavaObject(jsonFile, Video[].class);
        final Page<Video> pagesVideos = new PageImpl<>(Arrays.stream(videos).toList());

        final var videosDto = this.videoMapper.maptoList(Arrays.stream(videos).toList());
        when(this.videoRepository.findAll(any(Pageable.class))).thenReturn(pagesVideos);
        when(this.videoMapper.maptoList(anyList())).thenReturn(videosDto);

        //when
        final var response = this.videoService.getVideos(Pageable.ofSize(10));

        //Then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getContent()).usingRecursiveComparison().isEqualTo(videosDto);
    }

    @Test
    void save_response_ok_test() {
        //Given
        final var jsonFile = getJsonFile("create_video_response_ok.json");
        final var videoDto = parseToJavaObject(jsonFile, VideoDto.class);
        final var videoModel = this.videoMapper.mapToModel(videoDto);
        when(this.videoMapper.mapToModel(any(VideoDto.class))).thenReturn(videoModel);
        when(this.videoMapper.mapToVideoDto(any())).thenReturn(videoDto);

        //When
        final var videoCreated = this.videoService.save(videoDto);

        //then
        Assertions.assertThat(videoCreated).isNotNull();
        Assertions.assertThat(videoCreated).usingRecursiveComparison().isEqualTo(videoDto);
    }

    @Test
    void updateMovie_response_ok_test() {

        //Given
        final var jsonFile = getJsonFile("update_video_response_ok.json");
        final var updateVideoDtoParsed = parseToJavaObject(jsonFile, UpdateVideoDto.class);
        final var model = this.videoMapper.mapToModel(updateVideoDtoParsed);
        final var updateVideoDto = this.videoMapper.mapToUpdateVideoDto(model);
        when(this.videoMapper.mapToModel(any(UpdateVideoDto.class))).thenReturn(model);
        when(this.videoMapper.mapToUpdateVideoDto(any())).thenReturn(updateVideoDto);

        //When
        final var response = this.videoService.updateMovie(updateVideoDtoParsed);

        //Then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response).usingRecursiveComparison().isEqualTo(updateVideoDtoParsed);
    }

    @Test
    void delete_response_ok_test() {
        //Given
        final var jsonFile = getJsonFile("delete_video_response_ok.json");
        final var model = parseToJavaObject(jsonFile, Video.class);
        final var videoDto = this.videoMapper.mapToVideoDto(model);
        when(this.videoRepository.findById(anyString())).thenReturn(Optional.of(model));
        when(this.videoMapper.mapToVideoDto(model)).thenReturn(videoDto);

        //When
        final var response = this.videoService.delete("63680c011892283477b3e9b9");

        //Then
        Assertions.assertThat(response.get()).usingRecursiveComparison().isEqualTo(videoDto);
    }

    @Test
    void getById_response_ok_test() {
        //Given
        final var jsonFile = getJsonFile("getById_video_response_ok.json");
        final var model = parseToJavaObject(jsonFile, Video.class);
        when(videoRepository.findById(anyString())).thenReturn(Optional.of(model));

        //When
        final var response = this.videoService.getById("63680c011892283477b3e9b9");

        //Then
        Assertions.assertThat(response).usingRecursiveComparison().isEqualTo(this.videoMapper.mapToVideoDto(model));
    }

    @Test
    void getVideosByTitle_response_ok() {
        //Given
        final var jsonFile = getJsonFile("getById_video_response_ok.json");
        final var video = parseToJavaObject(jsonFile, Video.class);
        final var videoDtoExpected = this.videoMapper.mapToVideoDto(video);
        when(this.videoRepository.findByTitleLike(anyString())).thenReturn(List.of(video));

        //When
        final var response = this.videoService.getVideosByTitle("Fantasy");

        //Then
        Assertions.assertThat(response.get(0)).usingRecursiveComparison().isEqualTo(videoDtoExpected);
    }
}