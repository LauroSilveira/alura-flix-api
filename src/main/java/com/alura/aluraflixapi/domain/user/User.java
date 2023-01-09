package com.alura.aluraflixapi.domain.user;

import com.alura.aluraflixapi.domain.roles.Roles;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Document(collection = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
public class User implements Serializable, UserDetails {

  @Serial
  private static final long serialVersionUID = -7946742874682004844L;

  @Id
  private String id;

  private String username;

  private String password;

  @DBRef
  private List<Roles> roles;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.getRoles();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
