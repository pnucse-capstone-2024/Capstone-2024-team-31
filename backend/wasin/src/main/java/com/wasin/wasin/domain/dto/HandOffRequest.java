package com.wasin.wasin.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class HandOffRequest {

    public record UserRouterDTO(
        @NotEmpty(message = "라우터 리스트는 비어있으면 안됩니다.")
        @NotNull(message = "라우터 리스트는 비어있으면 안됩니다.")
        List<@Valid RouterDTO> router
    ) {
    }

    public record RouterDTO(
            @NotEmpty(message = "라우터 ssid는 비어있으면 안됩니다.")
            @Size(max = 255, message = "라우터 ssid는  255자 이내여야 합니다.")
            String ssid,

            @NotEmpty(message = "라우터 MAC 주소는 비어있으면 안됩니다.")
            @Size(max = 255, message = "라우터 MAC 주소는  255자 이내여야 합니다.")
            String macAddress,

            @NotNull(message = "신호세기는 비어있으면 안됩니다.")
            Long level,

            @NotNull(message = "정확한 신호세기는 비어있으면 안됩니다.")
            Long detailLevel
    ) {
    }
}
