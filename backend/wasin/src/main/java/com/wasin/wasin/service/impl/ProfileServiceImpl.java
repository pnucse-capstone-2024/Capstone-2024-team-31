package com.wasin.wasin.service.impl;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.NotFoundException;
import com.wasin.wasin._core.util.SshConnectionUtil;
import com.wasin.wasin.domain.dto.ProfileResponse;
import com.wasin.wasin.domain.entity.Company;
import com.wasin.wasin.domain.entity.Profile;
import com.wasin.wasin.domain.entity.Router;
import com.wasin.wasin.domain.entity.User;
import com.wasin.wasin.domain.mapper.ProfileMapper;
import com.wasin.wasin.domain.validation.CompanyValidation;
import com.wasin.wasin.domain.validation.ProfileValidation;
import com.wasin.wasin.repository.ProfileJPARepository;
import com.wasin.wasin.repository.RouterJPARepository;
import com.wasin.wasin.repository.UserJPARepository;
import com.wasin.wasin.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileMapper profileMapper;
    private final ProfileValidation profileValidation;
    private final CompanyValidation companyValidation;
    private final ProfileJPARepository profileJPARepository;
    private final RouterJPARepository routerJPARepository;
    private final UserJPARepository userJPARepository;
    private final SshConnectionUtil sshConnectionUtil;

    public ProfileResponse.FindAll findAll(User userDetails) {
        List<Profile> profileList = profileJPARepository.findAll();
        User user = findUserById(userDetails.getId());
        return profileMapper.profileToFindAllDTO(profileList, user);
    }

    @Transactional
    public void changeModeAuto(User userDetails) {
        User user = findUserById(userDetails.getId());
        profileValidation.checkIsManual(user.getCompany());
        user.getCompany().changeModeAuto();
    }

    @Transactional
    public void changeModeManual(User userDetails) {
        User user = findUserById(userDetails.getId());
        profileValidation.checkIsAuto(user.getCompany());
        user.getCompany().changeModeManual();
    }

    @Transactional
    public void changeProfile(User userDetails, Long profileId) {
        User user = findUserById(userDetails.getId());
        Company company = user.getCompany();
        Profile profile = findProfileById(profileId);
        List<Router> routerList = routerJPARepository.findAllRouterByCompanyId(company.getId());

        profileValidation.checkChangeable(company, profileId);
        companyValidation.checkLastUpdated(company.getLastUpdated());

        user.getCompany().addProfile(profile);
        for (Router router : routerList) {
            String command = "cd ./profile_execute; ./" + profile.getSsh();
            sshConnectionUtil.connect(command, router);
        }
    }

    private Profile findProfileById(Long profileId) {
        return profileJPARepository.findById(profileId).orElseThrow(
                () -> new NotFoundException(BaseException.PROFILE_NOT_FOUND));
    }

    private User findUserById(Long userId) {
        return userJPARepository.findActiveUserWithCompanyById(userId)
                .orElseThrow(() -> new NotFoundException(BaseException.USER_NOT_FOUND));
    }

}
