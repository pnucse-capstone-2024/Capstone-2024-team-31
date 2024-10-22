package com.wasin.wasin.domain.validation;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.BadRequestException;
import com.wasin.wasin.domain.entity.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailValidation {


    public void checkCodeValid(Email email, String code) {
        boolean isNotSame = !email.getCode().equals(code);
        if (isNotSame) {
            throw new BadRequestException(BaseException.EMAIL_CODE_WRONG);
        }
        if (email.isExpired()) {
            throw new BadRequestException(BaseException.EMAIL_EXPIRED);
        }
    }

    public void checkVerified(Email email) {
        if (!email.getIsVerified()) {
            throw new BadRequestException(BaseException.EMAIL_NOT_VERIFIED);
        }
    }
}
