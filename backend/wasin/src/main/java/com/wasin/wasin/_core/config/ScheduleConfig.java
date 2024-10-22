package com.wasin.wasin._core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.NotFoundException;
import com.wasin.wasin._core.exception.error.ServerException;
import com.wasin.wasin._core.util.SshConnectionUtil;
import com.wasin.wasin._core.util.web_api.GrafanaApiUtil;
import com.wasin.wasin.domain.dto.ProfileDTO;
import com.wasin.wasin.domain.entity.Company;
import com.wasin.wasin.domain.entity.Profile;
import com.wasin.wasin.domain.entity.Router;
import com.wasin.wasin.domain.entity.User;
import com.wasin.wasin.domain.mapper.ProfileMapper;
import com.wasin.wasin.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
@Configuration
public class ScheduleConfig {

    private final ObjectMapper om;
    private final ProfileMapper profileMapper;

    private final SshConnectionUtil sshConnectionUtil;
    private final GrafanaApiUtil grafanaApiUtil;

    private final ProfileJdbcRepository profileJdbcRepository;
    private final ProfileJPARepository profileJPARepository;
    private final CompanyRepository companyRepository;
    private final RouterJPARepository routerJPARepository;
    private final UserJPARepository userJPARepository;


    @PostConstruct
    public void init() {
        scheduleProfileSave();
        scheduleProfileUpdate();
    }

    @Transactional
    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void scheduleProfileSave() {
        try {
            ClassPathResource file = new ClassPathResource("static/profiles.json");
            InputStream inputstream = file.getInputStream();
            List<ProfileDTO> profileFileList = Arrays.asList(om.readValue(inputstream, ProfileDTO[].class));
            List<Profile> profileDBList = profileJPARepository.findAll();

            // DB 상에 있는 프로파일과 일치하지 않으면 업데이트
            if (shouldUpdate(profileFileList, profileDBList)) {
                List<Profile> profileList = profileMapper.dtoListToEntityList(profileFileList);
                profileJPARepository.deleteAllInBatch();
                profileJdbcRepository.saveAll(profileList);
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
            throw new ServerException(BaseException.FILE_READ_FAIL);
        }
    }


    @Transactional
    @Scheduled(fixedDelay = 1000 * 60 * 60)
    public void scheduleProfileUpdate() {
        try {
            // 변경해야 하는 라우터 그룹 목록: 프로파일 자동 변경 모드이면서 파워 세이빙 모드인 회사들
            List<Company> companyList = companyRepository.findAutoPowerSavingCompany();

            // 디폴트 프로파일로 변경 (타겟 프로파일)
            Profile profile = findDefaultProfile();

            // ssh 접속해서 해당 라우터 그룹의 모드 모두 변경, DB에도 변경
            updateProfile(companyList, profile);
        } catch (Exception e) {
            log.debug(e.getMessage());
            throw new ServerException(BaseException.POWER_SAVING_MODE_CHANGE_FAIL);
        }
    }

    private void updateProfile(List<Company> companyList, Profile profile) {
        for (Company company : companyList) {
            // 해당 라우터 그룹의 모든 라우터들
            List<User> userList = userJPARepository.findAllAdminByCompanyId(company.getId());
            List<Router> routerList = routerJPARepository.findAllRouterByCompanyId(company.getId());

            // 활성 사용자가 현재 시점으로 1명이라도 있다면 프로파일 변경
            if (isActiveUserExist(routerList)) {
                sshConnectionUtil.profileChangeAndSendAlarm(company, userList, routerList, profile);
            }
        }
    }

    private Boolean isActiveUserExist(List<Router> routerList) {
        for (Router router : routerList) {
            Long user = grafanaApiUtil.getActiveUser(router);
            if (user > 0) return true;
        }
        return false;
    }

    private Profile findDefaultProfile() {
        return profileJPARepository.findByIndex(1L).orElseThrow(
                () -> new NotFoundException(BaseException.PROFILE_NOT_FOUND)
        );
    }


    private boolean shouldUpdate(List<ProfileDTO> profileFileList, List<Profile> profileDBList) {
        List<ProfileDTO> profileDTODBList = profileMapper.entityListToDtoList(profileDBList);

        return profileFileList.size() != profileDTODBList.size() ||
                !new HashSet<>(profileFileList).containsAll(profileDTODBList);
    }


}
