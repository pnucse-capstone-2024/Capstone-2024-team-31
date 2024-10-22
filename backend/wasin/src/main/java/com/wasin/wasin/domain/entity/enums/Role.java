package com.wasin.wasin.domain.entity.enums;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.BadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;


@RequiredArgsConstructor
public enum Role {
    SUPER_ADMIN("super_admin"),
    ADMIN("admin"),
    USER("user");

    @Getter
    private final String eng;

    public static Role valueOfRole(String value) {
        return Arrays.stream(values())
                .filter(it -> it.eng.equals(value))
                .findFirst()
                .orElseThrow(() ->new BadRequestException(BaseException.USER_ROLE_WRONG));
    }

}
