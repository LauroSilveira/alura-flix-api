package com.alura.aluraflixapi.infraestructure.repository;

import com.alura.aluraflixapi.domain.video.Video;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface VideoRepository extends MongoRepository<Video, String> {

  @Query("{'category.id' : ?0}")
  List<Video> findVideosByCategories(String categoryId);
}
