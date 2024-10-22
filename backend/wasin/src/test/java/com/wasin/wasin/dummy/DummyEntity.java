package com.wasin.wasin.dummy;

import com.wasin.wasin.domain.dto.UserResponse;
import com.wasin.wasin.domain.entity.Token;
import com.wasin.wasin.domain.entity.User;
import com.wasin.wasin.domain.entity.enums.Role;
import com.wasin.wasin.domain.entity.enums.Status;

public class DummyEntity {
    public static User getTestUser(String email, Long id, Role role, Status status) {
        return User.builder()
                .username("테스트 유저")
                .id(id)
                .email(email)
                .password("user1234")
                .role(role)
                .status(status)
                .build();
    }

    public static Token getTestToken(UserResponse.Token token, String email) {
        return Token.builder()
                .accessToken(token.accessToken())
                .refreshToken(token.refreshToken())
                .email(email)
                .build();
    }

}
