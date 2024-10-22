package com.wasin.wasin.service.impl;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.NotFoundException;
import com.wasin.wasin.domain.dto.BackOfficeRequest;
import com.wasin.wasin.domain.dto.BackOfficeResponse;
import com.wasin.wasin.domain.entity.User;
import com.wasin.wasin.domain.mapper.BackOfficeMapper;
import com.wasin.wasin.domain.validation.BackOfficeValidation;
import com.wasin.wasin.repository.UserJPARepository;
import com.wasin.wasin.service.BackOfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BackOfficeServiceImpl implements BackOfficeService {

    private final UserJPARepository userJPARepository;
    private final BackOfficeValidation backOfficeValidation;
    private final BackOfficeMapper backOfficeMapper;

    @Transactional
    public void accept(User user, BackOfficeRequest.AcceptDTO requestDTO) {
        User superAdmin = findUserById(user.getId());
        User admin = findUserById(requestDTO.userId());
        backOfficeValidation.checkAccept(superAdmin, admin);
        admin.makeActive();
    }

    public BackOfficeResponse.WaitingList findWaitingList(User user) {
        User superAdmin = findUserWithCompanyById(user.getId());
        List<User> adminList = userJPARepository.findAllStandbyAdminByCompanyId(superAdmin.getCompany().getId());
        return backOfficeMapper.findWaitingList(adminList);
    }

    private User findUserById(Long userId) {
        return userJPARepository.findById(userId).orElseThrow(
                () -> new NotFoundException(BaseException.USER_NOT_FOUND)
        );
    }

    private User findUserWithCompanyById(Long userId) {
        return userJPARepository.findUserWithCompanyById(userId).orElseThrow(
                () -> new NotFoundException(BaseException.COMPANY_USER_NOT_FOUND)
        );
    }
}
