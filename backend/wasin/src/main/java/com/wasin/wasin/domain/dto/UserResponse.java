package com.wasin.wasin.domain.dto;

public class UserResponse {

    public record Token (
            String accessToken,
            String refreshToken
    ) {
    }

}
