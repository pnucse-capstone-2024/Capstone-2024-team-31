package com.wasin.wasin.service;

import com.wasin.wasin.domain.dto.HandOffRequest;
import com.wasin.wasin.domain.dto.HandOffResponse;
import com.wasin.wasin.domain.entity.User;

public interface HandOffService {
    HandOffResponse.UserRouter findAll(User user, HandOffRequest.UserRouterDTO requestDTO);

    HandOffResponse.BestRouter findBestRouter(User user, HandOffRequest.UserRouterDTO requestDTO);

    void changeModeAuto(User user);

    void changeModeManual(User user);
}
