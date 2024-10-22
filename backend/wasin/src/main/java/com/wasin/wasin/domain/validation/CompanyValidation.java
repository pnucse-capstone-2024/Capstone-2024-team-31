package com.wasin.wasin.domain.validation;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.BadRequestException;
import com.wasin.wasin.domain.dto.CompanyRequest;
import com.wasin.wasin.domain.entity.Company;
import com.wasin.wasin.domain.entity.User;
import com.wasin.wasin.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompanyValidation {

    @Value("${security.service-key}")
    private String serviceKey;

    private final CompanyRepository companyRepository;

    public void checkCompanyByOpenAPI(CompanyRequest.CompanyDTO companyDTO, User user, Optional<Company> company) {
        checkServiceKey(companyDTO.serviceKey());
        checkCompany(user, company);
    }

    private void checkServiceKey(String key) {
        if (!serviceKey.equals(key)) {
            throw new BadRequestException(BaseException.WRONG_WASIN_SERVICE_KEY);
        }
    }

    private void checkCompany(User user, Optional<Company> company) {
        if (user.getCompany() == null) {
            if (company.isPresent()) {
                throw new BadRequestException(BaseException.COMPANY_ALREADY_EXIST);
            }
        }
        else {
            if (company.isPresent()) {
                if (!company.get().getId().equals(user.getCompany().getId())) {
                    throw new BadRequestException(BaseException.COMPANY_WRONG);
                }
            }
            else {
                throw new BadRequestException(BaseException.USER_COMPANY_EXIST);
            }
        }

    }

    public void checkLastUpdated(LocalDateTime lastUpdated) {
        if (lastUpdated == null) return;

        Duration duration = Duration.between(lastUpdated, LocalDateTime.now());
        if (duration.toMinutes() < 30) {
            throw new BadRequestException(BaseException.PROFILE_UPDATED_JUST_NOW);
        }
    }

}
