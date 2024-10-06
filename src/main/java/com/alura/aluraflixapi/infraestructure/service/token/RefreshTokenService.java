package com.alura.aluraflixapi.infraestructure.service.token;

import com.alura.aluraflixapi.domain.token.RefreshToken;
import com.alura.aluraflixapi.infraestructure.repository.RefreshTokenRepository;
import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import com.alura.aluraflixapi.infraestructure.security.TokenService;
import com.alura.aluraflixapi.infraestructure.security.dto.TokenJwtDto;
import com.auth0.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RefreshTokenService {

    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, TokenService tokenService, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    public TokenJwtDto refreshAccessToken(final String refreshToken) {
        log.info("[RefreshTokenService] - Generating new accessToken");
        //valid if refresh token is expired
        tokenService.isRefreshTokenExpired(refreshToken);
        final var user = userRepository.findByUsername(JWT.decode(refreshToken).getSubject());
        final var accessToken = tokenService.generateTokenJwt(user);
        final var refreshTokenEntity = RefreshToken.buildRefreshTokenEntity(user, accessToken);
        refreshTokenRepository.save(refreshTokenEntity);
        return new TokenJwtDto(accessToken, refreshToken);
    }

}
