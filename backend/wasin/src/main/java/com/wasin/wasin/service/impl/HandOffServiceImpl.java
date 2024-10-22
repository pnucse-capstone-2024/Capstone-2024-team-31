package com.wasin.wasin.service.impl;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.NotFoundException;
import com.wasin.wasin._core.util.web_api.GrafanaApiUtil;
import com.wasin.wasin.domain.dto.HandOffRequest;
import com.wasin.wasin.domain.dto.HandOffResponse;
import com.wasin.wasin.domain.entity.Router;
import com.wasin.wasin.domain.entity.User;
import com.wasin.wasin.domain.validation.HandOffValidation;
import com.wasin.wasin.repository.RouterJPARepository;
import com.wasin.wasin.repository.UserJPARepository;
import com.wasin.wasin.service.HandOffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HandOffServiceImpl implements HandOffService {

    private final UserJPARepository userJPARepository;
    private final RouterJPARepository routerJPARepository;
    private final HandOffValidation handOffValidation;
    private final GrafanaApiUtil grafanaApiUtil;

    public HandOffResponse.UserRouter findAll(User userDetails, HandOffRequest.UserRouterDTO requestDTO) {
        User user = findUserById(userDetails.getId());
        return  new HandOffResponse.UserRouter(
                user.getIsModeAuto(),
                findSortedRouterList(requestDTO)
        );
    }

    public HandOffResponse.BestRouter findBestRouter(User userDetails, HandOffRequest.UserRouterDTO requestDTO) {
        User user = findUserById(userDetails.getId());
        return new HandOffResponse.BestRouter(
                user.getIsModeAuto(),
                findSortedRouterList(requestDTO).get(0)
        );
    }

    @Transactional
    public void changeModeAuto(User userDetails) {
        User user = findUserById(userDetails.getId());
        handOffValidation.checkIsManual(user.getIsModeAuto());
        user.changeModeAuto();
    }

    @Transactional
    public void changeModeManual(User userDetails) {
        User user = findUserById(userDetails.getId());
        handOffValidation.checkIsAuto(user.getIsModeAuto());
        user.changeModeManual();
    }

    private List<HandOffResponse.RouterWithStateDTO> findSortedRouterList(HandOffRequest.UserRouterDTO requestDTO) {
        return requestDTO.router().stream().map(it -> {
            Optional<Router> router = routerJPARepository.findByMacAddress(it.macAddress());
            return new HandOffResponse.RouterWithStateDTO(
                    it.level(),
                    it.detailLevel(),
                    getScore(it.level(), router),
                    it.ssid(),
                    it.macAddress(),
                    getPassword(router),
                    router.isPresent()
            );
        }).sorted().toList();
    }

    private String getPassword(Optional<Router> router) {
        if (router.isPresent()) {
            return router.get().getPassword();
        }
        return "";
    }

    private Long getScore(Long level, Optional<Router> router) {
        long w = 0;
        double r = 0.3;
        if (router.isPresent()) {
            w = grafanaApiUtil.getWifiState(router.get());
        }
        return (long) (level * 25 * (1 - r) + (w * r));
    }

    private User findUserById(Long id) {
        return userJPARepository.findById(id).orElseThrow(
                () -> new NotFoundException(BaseException.USER_NOT_FOUND)
        );
    }

}
