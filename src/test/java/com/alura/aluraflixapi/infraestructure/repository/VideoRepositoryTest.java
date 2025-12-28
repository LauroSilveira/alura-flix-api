package com.alura.aluraflixapi.infraestructure.repository;

import com.alura.aluraflixapi.domain.video.Video;
import com.alura.aluraflixapi.jsonutils.ParseJson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
class VideoRepositoryTest extends ParseJson {
    private static final String ROOT_PATH = "/category/";

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @AfterEach
    void after() {
        videoRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void findVideosByCategoriesTest() {
        //Given
        final var jsonFile = getJsonFile(ROOT_PATH + "get_all_videos_response_ok.json");
        final var videos = parseToList(jsonFile, Video.class);

        //necessary because Mongo does not have cascade
        videos.forEach(video -> this.categoryRepository.save(video.getCategory()));
        this.videoRepository.saveAll(videos);

        //When
        final var videosByCategories = this.videoRepository.findVideosByCategories(
                videos.stream().findFirst().get().getCategory().getId());

        //Then
        assertThat(videos.stream().findFirst().get())
                .usingRecursiveComparison()
                .isEqualTo(videosByCategories.stream().findFirst().get());
    }

    @Test
    void findVideoByTitleTest() {
        final var jsonFile = getJsonFile(ROOT_PATH + "get_all_videos_response_ok.json");
        final var videos = parseToList(jsonFile, Video.class);

        //necessary because Mongo does not have cascade
        videos.forEach(video -> this.categoryRepository.save(video.getCategory()));

        this.videoRepository.saveAll(videos);

        //When
        List<Video> videosEntity = this.videoRepository.findByTitleLike(
                videos.stream().findFirst().get().getTitle());

        //Then
        assertThat(videos.stream().findFirst().get())
                .usingRecursiveComparison()
                .isEqualTo(videosEntity.stream().findFirst().get());
    }
}