package com.wasin.wasin.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CompanyRequest {

    public record CompanyDTO(

            @NotEmpty(message = "서비스키는 비어있으면 안됩니다.")
            @Size(max = 255, message = "서비스키는 255자 이내여야 합니다.")
            String serviceKey,

            @NotEmpty(message = "회사 companyFssId는 비어있으면 안됩니다.")
            @Size(max = 255, message = "회사 companyFssId는 255자 이내여야 합니다.")
            String companyFssId,

            @NotEmpty(message = "회사 위치는 비어있으면 안됩니다.")
            @Size(max = 255, message = "회사 위치는 255자 이내여야 합니다.")
            String location,

            @NotEmpty(message = "회사 이름은 비어있으면 안됩니다.")
            @Size(max = 255, message = "회사 위치는 255자 이내여야 합니다.")
            String companyName
    ) {
    }

    public record CompanyByDB(
            @NotNull(message = "회사 id는 비어있으면 안됩니다.")
            Long companyId
    ) {
    }

    public record FindCompanyByOpenAPI(
            @NotEmpty(message = "회사 이름은는 비어있으면 안됩니다.")
            @Size(max = 255, message = "회사 위치는 255자 이내여야 합니다.")
            String companyName
    ) {
    }
}
