package com.alura.aluraflixapi.domain.user;

import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * User Details Service Impl that connect to database and retrieve a user previously registred
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private UserRepository repository;

  @Autowired
  public UserDetailsServiceImpl(UserRepository repository) {
    this.repository = repository;
  }

  /**
   * Search in databse a user by username and return
   * @param username
   * @return UserDestails
   * @throws UsernameNotFoundException
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.repository.findByusername(username);
  }
}
