package com.alura.aluraflixapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "video")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Video {

  private String id;

  private String title;

  private String description;

  private String url;

}
