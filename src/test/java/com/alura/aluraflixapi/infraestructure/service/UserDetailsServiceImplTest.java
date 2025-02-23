package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.domain.user.roles.Roles;
import com.alura.aluraflixapi.domain.user.roles.RolesEnum;
import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import com.alura.aluraflixapi.infraestructure.service.user.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private UserRepository userRepository;

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