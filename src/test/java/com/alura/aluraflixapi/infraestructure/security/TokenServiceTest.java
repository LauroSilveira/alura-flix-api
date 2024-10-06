package com.alura.aluraflixapi.infraestructure.security;

import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.domain.user.roles.Roles;
import com.alura.aluraflixapi.domain.user.roles.RolesEnum;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class TokenServiceTest {

    @SpyBean
    private TokenService tokenService;

    @Test
    @DisplayName("Should return a TokenJWT")
    void generateTokenJwt_test() {
        //Given
        final var user = User.builder().id(UUID.randomUUID().toString())
                .username("admin@aluraflix.com")
                .password("admin@aluraflix")
                .roles(Set.of(Roles.builder()
                        .id(UUID.randomUUID().toString())
                        .role(RolesEnum.ROLE_ADMIN)
                        .build()))
                .build();
        //When
        final var tokenJWT = this.tokenService.generateTokenJwt(user);
        //Then
        assertThat(tokenJWT)
                .isNotBlank()
                .isNotNull();
    }

    @Test
    @DisplayName("Should return a JWTCreationException when is generating TokenJWT and some field is null")
    void generateTokenJwt_KO_test() {
        //Then
        Assertions.assertThatExceptionOfType(JWTCreationException.class)
                .isThrownBy(() -> this.tokenService.generateTokenJwt(null))
                .withMessageContaining("Error to create JWT accessToken");
    }


    @Test
    @DisplayName("Should retrieve User Principal from TokenJWT passed")
    void verifyTokenJWT_test() {
        //Given
        final var user = User.builder().id(UUID.randomUUID().toString())
                .username("admin@aluraflix.com")
                .password("admin@aluraflix")
                .roles(Set.of(Roles.builder()
                        .id(UUID.randomUUID().toString())
                        .role(RolesEnum.ROLE_ADMIN)
                        .build()))
                .build();
        final var tokenJWT = this.tokenService.generateTokenJwt(user);
        //When
        final var userPrincipal = this.tokenService.verifyTokenJWT(tokenJWT);
        //Then

        assertThat(userPrincipal)
                .isNotNull()
                .isEqualTo("admin@aluraflix.com");
    }

    @Test
    @DisplayName("Should return a JWTDecodeException when is verifying TokenJWT and is null")
    void verifyTokenJWT_KO_test() {
        //Then
        Assertions.assertThatExceptionOfType(JWTVerificationException.class)
                .isThrownBy(() -> this.tokenService.verifyTokenJWT(null))
                .withMessageContaining("Error verifying JWT Token");
    }
}