package com.wasin.wasin.domain.validation;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.BadRequestException;
import com.wasin.wasin.domain.entity.User;
import com.wasin.wasin.domain.entity.enums.Role;
import com.wasin.wasin.domain.entity.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BackOfficeValidation {

    public void checkAccept(User superAdmin, User admin) {
        checkIfCompanySame(superAdmin, admin);
        checkIsAdmin(admin);
        checkIsStandBy(admin);
    }

    private void checkIfCompanySame(User superAdmin, User admin) {
        if (!superAdmin.getCompany().equals(admin.getCompany())) {
            throw new BadRequestException(BaseException.NOT_SAME_COMPANY);
        }
    }

    private static void checkIsStandBy(User admin) {
        if (admin.getStatus() != Status.STAND_BY) {
            throw new BadRequestException(BaseException.STATUS_NOT_STANDBY);
        }
    }

    private static void checkIsAdmin(User admin) {
        if (admin.getRole() != Role.ADMIN) {
            throw new BadRequestException(BaseException.ROLE_NOT_ADMIN);
        }
    }

}
