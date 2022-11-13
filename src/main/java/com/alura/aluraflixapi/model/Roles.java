package com.alura.aluraflixapi.model;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

/**
 * Class With GrandAuthorities of spring
 */
@Document(collection = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Roles implements GrantedAuthority, Serializable {

  @Serial
  private static final long serialVersionUID = -8909296276964731109L;

  private RoleEnum roleName;

  @Override
  public String getAuthority() {
    return this.getRoleName().name();
  }
}
