package com.wasin.wasin.service;

import com.wasin.wasin.domain.dto.UserRequest;

public interface MailService {
    void sendMail(UserRequest.EmailDTO requestDTO);

    void checkMailCode(UserRequest.EmailCheckDTO requestDTO);

    void checkVerified(String email);
}
