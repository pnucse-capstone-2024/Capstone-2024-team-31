package com.wasin.wasin.domain.dto;

import jakarta.validation.constraints.NotNull;

public class BackOfficeRequest {

    public record AcceptDTO(
            @NotNull(message = "AcceptDTO의 userId는 비어있으면 안됩니다.")
            Long userId
    ) {
    }

}
