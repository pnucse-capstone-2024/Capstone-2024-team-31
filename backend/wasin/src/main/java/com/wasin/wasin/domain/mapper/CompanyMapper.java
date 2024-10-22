package com.wasin.wasin.domain.mapper;

import com.wasin.wasin.domain.dto.CompanyRequest;
import com.wasin.wasin.domain.dto.CompanyResponse;
import com.wasin.wasin.domain.entity.Company;
import com.wasin.wasin.domain.entity.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyMapper {

    public Company openAPIDTOToCompany(CompanyRequest.CompanyDTO dto, Profile profile) {
        return Company.builder()
                .fssId(dto.companyFssId())
                .name(dto.companyName())
                .location(dto.location())
                .isAuto(true)
                .profile(profile)
                .build();
    }

    public CompanyResponse.DBList entityToDBListDTO(List<Company> companyList) {
        return new CompanyResponse.DBList(
                companyList.stream()
                        .map(company -> new CompanyResponse.DBList.CompanyDBItem(
                                company.getId(), company.getLocation(), company.getName())
                        )
                        .collect(Collectors.toList())
        );
    }
}
