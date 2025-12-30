package com.alura.aluraflixapi.infraestructure.service.token;

import com.alura.aluraflixapi.domain.token.RefreshToken;
import com.alura.aluraflixapi.infraestructure.repository.RefreshTokenRepository;
import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import com.alura.aluraflixapi.infraestructure.security.TokenService;
import com.alura.aluraflixapi.infraestructure.security.dto.TokenJwtDTO;
import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {

    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public TokenJwtDTO refreshAccessToken(final String refreshToken) {
        log.info("[RefreshTokenService] - Generating new accessToken");
        final var subject = JWT.decode(refreshToken).getSubject();
        //Validate if refresh token is expired
        tokenService.isRefreshTokenExpired(refreshToken);
        final var user = userRepository.findByUsername(subject)
                .orElseThrow(() -> new UsernameNotFoundException("username not found to user " + subject));
        final var accessToken = tokenService.generateTokenJwt(user);
        final var refreshTokenEntity = RefreshToken.buildRefreshTokenEntity(user, accessToken);
        refreshTokenRepository.save(refreshTokenEntity);
        return new TokenJwtDTO(accessToken, refreshToken);
    }

}
