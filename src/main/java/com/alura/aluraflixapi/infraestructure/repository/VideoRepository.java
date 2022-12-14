package com.alura.aluraflixapi.infraestructure.repository;

import com.alura.aluraflixapi.domain.video.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepository extends MongoRepository<Video, String> {

}
