package com.alura.aluraflixapi.domain.token;

import com.alura.aluraflixapi.domain.user.User;
import com.auth0.jwt.JWT;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RefreshToken {
    @Id
    private String id;
    private String token;
    private String userName;
    private Instant expiryDate;

    public static RefreshToken buildRefreshTokenEntity(User user, String accessToken) {
        return RefreshToken.builder()
                .userName(user.getUsername())
                .token(accessToken)
                .expiryDate(JWT.decode(accessToken).getExpiresAtAsInstant())
                .build();
    }
}
