package com.alura.aluraflixapi.repository;

import com.alura.aluraflixapi.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepository extends MongoRepository<Video, String> {

}
