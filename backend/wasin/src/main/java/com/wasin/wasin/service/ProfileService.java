package com.wasin.wasin.service;

import com.wasin.wasin.domain.dto.ProfileResponse;
import com.wasin.wasin.domain.entity.User;

public interface ProfileService {
    ProfileResponse.FindAll findAll(User userDetails);

    void changeModeAuto(User user);

    void changeModeManual(User use);

    void changeProfile(User user, Long profileId);

}
