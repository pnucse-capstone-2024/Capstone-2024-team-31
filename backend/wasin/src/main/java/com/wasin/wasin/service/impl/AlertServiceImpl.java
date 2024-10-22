package com.wasin.wasin.service.impl;

import com.wasin.wasin._core.util.SendFirebaseMessage;
import com.wasin.wasin._core.util.SshConnectionUtil;
import com.wasin.wasin.domain.dto.AlertRequest;
import com.wasin.wasin.domain.entity.Profile;
import com.wasin.wasin.domain.entity.Router;
import com.wasin.wasin.domain.entity.User;
import com.wasin.wasin.repository.ProfileJPARepository;
import com.wasin.wasin.repository.RouterJPARepository;
import com.wasin.wasin.repository.UserJPARepository;
import com.wasin.wasin.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
@Service
public class AlertServiceImpl implements AlertService {

    private final SshConnectionUtil sshConnectionUtil;
    private final SendFirebaseMessage sendFirebaseMessage;

    private final RouterJPARepository routerJPARepository;
    private final ProfileJPARepository profileJPARepository;
    private final UserJPARepository userJPARepository;

    private final String HEALTH_CHECK = "Health Check";

    @Transactional
    public void receiveAlert(AlertRequest.ProfileChangeDTO request) {
        request.alerts().forEach(alert -> {
            findRouter(alert).ifPresent(router -> {
                List<User> userList = getUserList(router);
                boolean isHealthCheck = alert.labels().alertname().equals(HEALTH_CHECK);

                if (isHealthCheck) {
                    healthCheck(router, userList);
                }
                else {
                    changeProfile(alert, router, userList);
                }
            });
        });
    }

    private void changeProfile(AlertRequest.AlertDTO alert, Router router, List<User> userList) {
        List<Router> routerList = getRouterList(router);
        findDefaultProfile(alert.labels().alertname()).ifPresent(profile ->
            sshConnectionUtil.profileChangeAndSendAlarm(router.getCompany(), userList, routerList, profile)
        );
    }

    private void healthCheck(Router router, List<User> userList) {
        String title = "[긴급🚨] 라우터 고장";
        String body = router.getName() + "라우터가 고장났습니다.";
        sendFirebaseMessage.sendFcmAlert(userList, router, title, body);
    }

    private List<Router> getRouterList(Router router) {
        return routerJPARepository.findAllRouterByCompanyId(router.getCompany().getId());
    }

    private List<User> getUserList(Router router) {
        return userJPARepository.findAllAdminByCompanyId(router.getCompany().getId());
    }

    private Optional<Router> findRouter(AlertRequest.AlertDTO alert) {
        return routerJPARepository.findByInstance(alert.labels().instance());
    }

    private Optional<Profile> findDefaultProfile(String ssh) {
        return profileJPARepository.findBySsh(ssh);
    }
}
