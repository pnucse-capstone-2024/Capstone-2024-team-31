package com.wasin.wasin.service;

import com.wasin.wasin.domain.dto.CompanyRequest;
import com.wasin.wasin.domain.dto.CompanyResponse;
import com.wasin.wasin.domain.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface CompanyService {
    CompanyResponse.OpenAPIList findAllCompanyByOpenAPI(CompanyRequest.FindCompanyByOpenAPI request);

    CompanyResponse.DBList findAllCompanyByDB();

    void saveCompanyByOpenAPI(CompanyRequest.CompanyDTO request, MultipartFile file, User user);

    void saveCompanyByDB(CompanyRequest.CompanyByDB request, User user);
}
