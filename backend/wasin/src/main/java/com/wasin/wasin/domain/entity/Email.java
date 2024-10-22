package com.wasin.wasin.domain.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@RedisHash(value = "email", timeToLive = 60 * 60) // 1시간
public class Email {

    @Id
    private String email;

    @Indexed
    private String code;

    private Boolean isVerified;

    private LocalDateTime createdAt;

    @Builder
    public Email(String email, String code, Boolean isVerified) {
        this.email = email;
        this.code = code;
        this.isVerified = isVerified;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        // 생성된지 5분이 지나면 만료되었다고 판단
        Duration duration = Duration.between(LocalDateTime.now(), this.createdAt);
        return duration.getSeconds() > 60*3;
    }

    public void updateVerified() {
        this.isVerified = true;
    }
}
