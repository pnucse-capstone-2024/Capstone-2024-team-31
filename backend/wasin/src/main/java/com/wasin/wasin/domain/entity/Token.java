package com.wasin.wasin.domain.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "token", timeToLive = 60 * 60 * 24 * 365 * 10)
public class Token {

    @Id
    private String refreshToken;

    @Indexed
    private String accessToken;

    private Long userId;

    private String email;

    @Builder
    public Token(String refreshToken, String accessToken, Long userId, String email) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.userId = userId;
        this.email = email;
    }
}
