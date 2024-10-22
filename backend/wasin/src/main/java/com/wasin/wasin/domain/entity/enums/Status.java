package com.wasin.wasin.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {
    STAND_BY("stand_by"),
    ACTIVE("active"),
    INACTIVE("inactive");

    @Getter
    private final String eng;

    public static Status getDefaultStatusByRole(Role role) {
        if (role.equals(Role.ADMIN)) return STAND_BY;
        return ACTIVE;
    }
}
