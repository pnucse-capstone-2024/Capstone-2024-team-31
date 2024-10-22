package com.wasin.wasin.domain.validation;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.ForbiddenException;
import com.wasin.wasin.domain.entity.Router;
import com.wasin.wasin.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonitoringValidation {


    public void checkRouterExistInGroup(Router router, User user) {
        Long routerCompanyId = router.getCompany().getId();
        Long userCompanyId = user.getCompany().getId();
        if (!routerCompanyId.equals(userCompanyId)) {
            throw new ForbiddenException(BaseException.MONITOR_PERMISSION_NONE);
        }
    }
}
