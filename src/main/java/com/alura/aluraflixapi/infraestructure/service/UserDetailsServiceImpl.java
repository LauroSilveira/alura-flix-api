package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * User Details Service Impl that connect to database and retrieve a user previously registred
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private static final String PREFIX_LOGGING = "[UserDetailsServiceImpl]";

  private final UserRepository repository;

  @Autowired
  public UserDetailsServiceImpl(UserRepository repository) {
    this.repository = repository;
  }

  /**
   * Search in databse a user by username and return it
   * @return UserDestails
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("{} Retrieving User by username: {}", PREFIX_LOGGING, username);
    final var user = this.repository.findByUsername(username);
    log.info("{} Retrieved User: {}", PREFIX_LOGGING, user);
    return user;
  }
}
