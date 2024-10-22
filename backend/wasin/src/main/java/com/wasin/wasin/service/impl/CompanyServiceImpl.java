package com.wasin.wasin.service.impl;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.NotFoundException;
import com.wasin.wasin._core.util.AwsFileUtil;
import com.wasin.wasin._core.util.web_api.WebApiUtil;
import com.wasin.wasin.domain.dto.CompanyDTO;
import com.wasin.wasin.domain.dto.CompanyRequest;
import com.wasin.wasin.domain.dto.CompanyResponse;
import com.wasin.wasin.domain.entity.Company;
import com.wasin.wasin.domain.entity.CompanyImage;
import com.wasin.wasin.domain.entity.Profile;
import com.wasin.wasin.domain.entity.User;
import com.wasin.wasin.domain.mapper.CompanyImageMapper;
import com.wasin.wasin.domain.mapper.CompanyMapper;
import com.wasin.wasin.domain.validation.CompanyValidation;
import com.wasin.wasin.repository.CompanyImageRepository;
import com.wasin.wasin.repository.CompanyRepository;
import com.wasin.wasin.repository.ProfileJPARepository;
import com.wasin.wasin.repository.UserJPARepository;
import com.wasin.wasin.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CompanyServiceImpl implements CompanyService {

    private final WebApiUtil webApiUtil;

    private final UserJPARepository userJPARepository;
    private final CompanyImageRepository companyImageRepository;
    private final CompanyRepository companyRepository;
    private final ProfileJPARepository profileJPARepository;

    private final CompanyValidation companyValidation;
    private final CompanyMapper companyMapper;
    private final CompanyImageMapper companyImageMapper;
    private final AwsFileUtil awsFileUtil;

    public CompanyResponse.OpenAPIList findAllCompanyByOpenAPI(CompanyRequest.FindCompanyByOpenAPI request) {
        CompanyDTO.ResponseValue companyList = webApiUtil.getCompanyList(request.companyName());
        return new CompanyResponse.OpenAPIList(
                companyList.response().body().items().item()
                        .stream()
                        .map(CompanyServiceImpl::getCompanyOpenAPIItem)
                        .collect(Collectors.toList())
        );
    }

    public CompanyResponse.DBList findAllCompanyByDB() {
        List<Company> companyList = companyRepository.findAll();
        return companyMapper.entityToDBListDTO(companyList);
    }

    @Transactional
    public void saveCompanyByOpenAPI(CompanyRequest.CompanyDTO request, MultipartFile file, User user) {
        User admin = findAdminById(user.getId());

        // 1. 들어온 요청이 유효한 값인지 확인
        Optional<Company> companyByFssId = companyRepository.findByFssId(request.companyFssId());
        companyValidation.checkCompanyByOpenAPI(request, admin, companyByFssId);

        // 2. AWS에 이미지 업로드
        String url = awsFileUtil.upload(file);

        // 3. DB 내에 회사 저장
        Profile profile = findDefaultProfile();
        Company company = companyByFssId.orElseGet(() -> companyMapper.openAPIDTOToCompany(request, profile));
        companyRepository.save(company);

        // 4. DB 내에 이미지 저장
        CompanyImage image = companyImageMapper.urlToCompanyImage(url, file, company);
        companyImageRepository.deleteByCompanyId(company.getId());
        companyImageRepository.save(image);

        // 5. 관리자에게 회사 등록해주기
        admin.joinCompany(company);
    }

    @Transactional
    public void saveCompanyByDB(CompanyRequest.CompanyByDB request, User user) {
        Company company = companyRepository.findById(request.companyId()).orElseThrow(
                () -> new NotFoundException(BaseException.COMPANY_NOT_FOUND)
        );

        // 관리자에게 회사 등록해주기
        User admin = findAdminById(user.getId());
        admin.joinCompany(company);
    }

    private Profile findDefaultProfile() {
        return profileJPARepository.findByIndex(1L).orElseThrow(
                () -> new NotFoundException(BaseException.PROFILE_NOT_FOUND)
        );
    }

    private User findAdminById(Long userId) {
        return userJPARepository.findById(userId).orElseThrow(
                () -> new NotFoundException(BaseException.USER_NOT_FOUND)
        );
    }

    private static CompanyResponse.OpenAPIList.CompanyOpenAPIItem getCompanyOpenAPIItem(CompanyDTO.Item item) {
        return new CompanyResponse.OpenAPIList.CompanyOpenAPIItem(
                item.crno() + "-" + item.bzno() + "-" + item.fssCorpUnqNo(),
                item.enpBsadr(),
                item.corpNm()
        );
    }
}
