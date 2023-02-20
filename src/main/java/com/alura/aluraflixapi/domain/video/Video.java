package com.alura.aluraflixapi.domain.video;

import com.alura.aluraflixapi.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "video")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Video {

  @Id
  private String id;

  private String title;

  private String description;

  private String url;

  @DBRef
  private Category categoryId;

}
