package com.alura.aluraflixapi.domain.user.roles;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Class With GrandAuthorities of spring
 */
@Document(collection = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Roles implements Serializable {

  @Serial
  private static final long serialVersionUID = -8909296276964731109L;

  private RoleEnum roleName;
}
