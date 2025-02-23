package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.controller.authentication.AuthenticationController;
import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.domain.user.dto.AuthenticationDto;
import com.alura.aluraflixapi.domain.user.roles.Roles;
import com.alura.aluraflixapi.domain.user.roles.RolesEnum;
import com.alura.aluraflixapi.infraestructure.security.TokenService;
import com.alura.aluraflixapi.infraestructure.service.token.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Test
    void login_test() {
        //Given
        final var tokenJwtFake = UUID.randomUUID().toString();
        final var userAdministrator = new User("1", "admin@aluraflix.com", "administrator",
                Set.of(Roles.builder().id("1").role(RolesEnum.ROLE_ADMIN).build()));

        final var testingAuthenticationToken = new TestingAuthenticationToken(userAdministrator, userAdministrator);

        when(this.authenticationManager.authenticate(any())).thenReturn(testingAuthenticationToken);
        when(this.tokenService.generateTokenJwt(any())).thenReturn(tokenJwtFake);
        when(this.tokenService.generateRefreshToken(any())).thenReturn(tokenJwtFake);
        //When
        final var userAuthenticated = this.authenticationController.login(new AuthenticationDto("admin@aluraflix.com", "administrator"));

        //Then
        assertThat(userAuthenticated).isNotNull();
        assertThat(userAuthenticated.getBody()).isNotNull();
        assertThat(userAuthenticated.getBody().accessToken()).isEqualTo(tokenJwtFake);
        assertThat(userAuthenticated.getBody().refreshToken()).isEqualTo(tokenJwtFake);
    }
}