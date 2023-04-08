package com.alura.aluraflixapi.infraestructure.repository;

import com.alura.aluraflixapi.domain.category.Category;
import com.alura.aluraflixapi.domain.category.Rating;
import com.alura.aluraflixapi.domain.video.Video;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

@DataMongoTest
@ActiveProfiles("test")
class VideoRepositoryTest {

  @Autowired
  private VideoRepository videoRepository;

  @Autowired
  private CategoryRepository categoryRepository;


  @AfterEach
  void after() {
    //TODO: search another way to delete all data without call this methods.
    videoRepository.deleteAll();
    categoryRepository.deleteAll();
  }

  @Test
  void findVideosByCategoriesTest() {
    //Given
    List<Video> videos = buildVideo();

    //necessary because Mongo does not have cascade
    videos.forEach(video -> this.categoryRepository.save(video.getCategory()));
    this.videoRepository.saveAll(videos);

    //When
    List<Video> videoRetrieved = this.videoRepository.findVideosByCategories(
        videos.stream().findFirst().get().getCategory().getId());

    //Then
    Assertions.assertThat(videos.stream().findFirst().get())
        .usingRecursiveComparison()
        .isEqualTo(videoRetrieved.stream().findFirst().get());
  }

  @Test
  void findVideoByTitleTest() {
    List<Video> videos = buildVideo();

    //necessary because Mongo does not have cascade
    videos.forEach(video -> this.categoryRepository.save(video.getCategory()));

    this.videoRepository.saveAll(videos);

    //When
    List<Video> videoRetrieved = this.videoRepository.findByTitleLike(
        videos.stream().findFirst().get().getTitle());

    //Then
    Assertions.assertThat(videos.stream().findFirst().get())
        .usingRecursiveComparison()
        .isEqualTo(videoRetrieved.stream().findFirst().get());
  }

  private List<Video> buildVideo() {
    return List.of(Video.builder()
            .id(UUID.randomUUID().toString())
            .url("www.lordoftherings.com")
            .title("Lord of the rings - Fellowship of the ring")
            .description("First movie of lord of the rings")
            .category(Category.builder()
                .id(UUID.randomUUID().toString())
                .title("Lorf of the rings - Fellowship ofg the ring")
                .rating(Rating.FANTASY.name())
                .colorHex("#ffff83")
                .build())
            .build(),
        Video.builder()
            .id(UUID.randomUUID().toString())
            .url("www.lordoftherings.com")
            .title("Lord of the rings - The two towers")
            .description("Second movie of lord of the rings")
            .category(Category.builder()
                .id(UUID.randomUUID().toString())
                .title("Lorf of the rings - The two towers")
                .rating(Rating.FANTASY.name())
                .colorHex("#ffff83")
                .build())
            .build(),
        Video.builder()
            .id(UUID.randomUUID().toString())
            .url("www.lordoftherings.com")
            .title("Lord of the rings - The return of the king")
            .description("Third movie of lord of the rings")
            .category(Category.builder()
                .id(UUID.randomUUID().toString())
                .title("Lorf of the rings - The return of the king")
                .rating(Rating.FANTASY.name())
                .colorHex("#ffff83")
                .build())
            .build());
  }
}