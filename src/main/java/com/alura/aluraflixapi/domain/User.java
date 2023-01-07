package com.alura.aluraflixapi.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User implements Serializable {

  @Serial
  private static final long serialVersionUID = 1557886448713650485L;

  private String id;

  private String username;

  private String password;

  private List<Roles> roles;
}
