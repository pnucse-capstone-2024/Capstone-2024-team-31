package com.wasin.wasin.domain.validation;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.BadRequestException;
import com.wasin.wasin._core.exception.error.ForbiddenException;
import com.wasin.wasin.domain.dto.RouterRequest;
import com.wasin.wasin.domain.entity.Router;
import com.wasin.wasin.domain.entity.User;
import com.wasin.wasin.repository.RouterJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouterValidation {

    private final RouterJPARepository routerJPARepository;

    public void checkRouterNonExistInDB(RouterRequest.CreateDTO requestDTO) {
        if(routerJPARepository.findByMacAddress(requestDTO.macAddress()).isPresent()) {
            throw new BadRequestException(BaseException.ROUTER_EXIST_IN_DB);
        }
    }

    public void checkRouterPermission(Router router, User user) {
        if (!user.getCompany().getId().equals(router.getCompany().getId())) {
            throw new ForbiddenException(BaseException.ROUTER_PERMISSION_DENIED);
        }
    }
}
