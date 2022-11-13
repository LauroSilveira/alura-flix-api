package com.alura.aluraflixapi.service;

import com.alura.aluraflixapi.model.User;
import com.alura.aluraflixapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User userModel = userRepository.findByusername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username not found" + username));

    // returns User of type UserdetailService of Spring
    return new org.springframework.security.core.userdetails.User(userModel.getUsername(),
        userModel.getPassword(), true, true, true, true, userModel.getRoles());
  }
}
