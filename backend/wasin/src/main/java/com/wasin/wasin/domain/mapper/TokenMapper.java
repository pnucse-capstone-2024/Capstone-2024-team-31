package com.wasin.wasin.domain.mapper;

import com.wasin.wasin.domain.entity.Token;
import com.wasin.wasin.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenMapper {

    public Token stringToRefreshToken(String accessToken, String refreshToken, User user) {

        return Token.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .email(user.getEmail())
                .userId(user.getId())
                .build();
    }
}
