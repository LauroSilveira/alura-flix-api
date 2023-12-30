package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.domain.user.roles.Roles;
import com.alura.aluraflixapi.domain.user.roles.RolesEnum;
import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserDetailsServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @SpyBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("Should find a user by username and return")
    void loadUserByUsername_test() {
        //Given
        final var user = new User(UUID.randomUUID().toString(), "user@aluraflix.com", "user123456",
                Set.of(Roles.builder().id(UUID.randomUUID().toString()).role(RolesEnum.ROLE_USER).build()));

        when(this.userRepository.findByUsername(anyString()))
                .thenReturn(user);

        //When
        final var userDetails = this.userDetailsService.loadUserByUsername(user.getUsername());
        //Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(user.getUsername());
        assertThat(userDetails.getPassword()).isEqualTo(user.getPassword());
        assertThat(userDetails.getAuthorities()).isEqualTo(user.getAuthorities());

    }
}