package com.wasin.wasin.service;

import com.wasin.wasin.domain.dto.UserRequest;
import com.wasin.wasin.domain.entity.User;

public interface UserService {

    void signup(UserRequest.SignUpDTO requestDTO);

    User login(UserRequest.LoginDTO requestDTO);

    void withdraw(User user);

    void setLockPassword(UserRequest.LockDTO requestDTO, User user);

    void checkLockPassword(UserRequest.LockConfirmDTO requestDTO, User user);

}
