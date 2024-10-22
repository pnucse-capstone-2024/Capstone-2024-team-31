package com.wasin.wasin.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RouterRequest {
    public record CreateDTO(
            @NotEmpty(message = "라우터 이름은 비어있으면 안됩니다.")
            @Size(max = 255, message = "라우터 이름은 255자 이내여야 합니다.")
            String name,

            @NotEmpty(message = "라우터 MAC 주소는 비어있으면 안됩니다.")
            @Size(max = 255, message = "라우터 MAC 주소는  255자 이내여야 합니다.")
            String macAddress,

            @NotEmpty(message = "라우터 serialNumber는 비어있으면 안됩니다.")
            @Size(max = 255, message = "라우터 serialNumber는  255자 이내여야 합니다.")
            String serialNumber,

            @NotEmpty(message = "라우터 password는 비어있으면 안됩니다.")
            @Size(max = 255, message = "라우터 password는  255자 이내여야 합니다.")
            String password,

            @NotNull(message = "라우터 포트번호는 비어있으면 안됩니다.")
            Integer port,

            @NotNull(message = "x축 위치는 비어있으면 안됩니다.")
            Double positionX,

            @NotNull(message = "y축 위치는 비어있으면 안됩니다.")
            Double positionY
    ) {
    }

    public record UpdateDTO(
            @NotEmpty(message = "라우터 이름은 비어있으면 안됩니다.")
            @Size(max = 255, message = "라우터 이름은 255자 이내여야 합니다.")
            String name,

            @NotEmpty(message = "라우터 password는 비어있으면 안됩니다.")
            @Size(max = 255, message = "라우터 password는  255자 이내여야 합니다.")
            String password,

            @NotNull(message = "x축 위치는 비어있으면 안됩니다.")
            Double positionX,

            @NotNull(message = "y축 위치는 비어있으면 안됩니다.")
            Double positionY
    ) {
    }
    
    public record LogDTO(
            @NotEmpty(message = "로그 내용은 비어있으면 안됩니다.")
            String log
    ) {
    }
}
