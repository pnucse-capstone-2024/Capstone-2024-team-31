package com.wasin.wasin.service;

import com.wasin.wasin.domain.dto.UserRequest;
import com.wasin.wasin.domain.dto.UserResponse;
import com.wasin.wasin.domain.entity.User;

public interface TokenService {

    UserResponse.Token save(User user);

    void delete(String accessToken);

    UserResponse.Token reissue(UserRequest.ReissueDTO requestDTO);
}
