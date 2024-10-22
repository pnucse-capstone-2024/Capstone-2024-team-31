package com.wasin.wasin.domain.dto;

import java.util.List;

public class ProfileResponse {

    public record FindAll(
            Boolean isAuto,
            Long activeProfileId,
            List<ProfileEachDTO> profiles
    ) {
    }

    public record ProfileEachDTO(
            Long profileId,
            String title,
            String description,
            String tip
    ) {
    }

}
