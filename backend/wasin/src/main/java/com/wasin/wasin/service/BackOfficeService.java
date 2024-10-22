package com.wasin.wasin.service;

import com.wasin.wasin.domain.dto.BackOfficeRequest;
import com.wasin.wasin.domain.dto.BackOfficeResponse;
import com.wasin.wasin.domain.entity.User;

public interface BackOfficeService {

    void accept(User user, BackOfficeRequest.AcceptDTO requestDTO);

    BackOfficeResponse.WaitingList findWaitingList(User user);

}
