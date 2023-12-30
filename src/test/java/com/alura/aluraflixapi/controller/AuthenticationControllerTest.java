package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.domain.user.dto.AuthenticationDto;
import com.alura.aluraflixapi.domain.user.roles.Roles;
import com.alura.aluraflixapi.domain.user.roles.RolesEnum;
import com.alura.aluraflixapi.infraestructure.security.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AuthenticationControllerTest {

    @SpyBean
    private AuthenticationController authenticationController;

    @MockBean
    private AuthenticationManager manager;

    @MockBean
    private TokenService tokenService;


    @Test
    void login_test() {
        //Given
        final var tokenJwtFake = UUID.randomUUID().toString();
        final var userAdministrator = new User("1", "admin@aluraflix.com", "administrator",
                Set.of(Roles.builder().id("1").role(RolesEnum.ROLE_ADMIN).build()));

        final var testingAuthenticationToken = new TestingAuthenticationToken(userAdministrator, userAdministrator);
        when(this.tokenService.generateTokenJWT(Mockito.any())).thenReturn(tokenJwtFake);
        when(this.manager.authenticate(Mockito.any())).thenReturn(testingAuthenticationToken);

        //When
        final var userAuthenticated = this.authenticationController.login(new AuthenticationDto("admin@aluraflix.com", "administrator"));

        //Then
        assertThat(userAuthenticated).isNotNull();
        assertThat(userAuthenticated.getBody()).isNotNull();
        assertThat(userAuthenticated.getBody().token()).isEqualTo(tokenJwtFake);
    }
}