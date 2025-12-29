package com.alura.aluraflixapi.infraestructure.service.user;

import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * User Details Service Impl that connect to database and retrieve a user previously registered
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
     * Search in database a user by username and return it
     * new UsernameNotFoundException(String.format("User not found for: %s", username));
     *
     * @return UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(@NonNull String username) {
        log.info("{} Retrieving User by username: {}", PREFIX_LOGGING, username);
        return this.repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(PREFIX_LOGGING + "User not found for:" + username));
    }
}
