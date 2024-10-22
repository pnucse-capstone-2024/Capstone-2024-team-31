package com.wasin.wasin.domain.validation;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HandOffValidation {


    public void checkIsAuto(Boolean isAuto) {
        if (!isAuto) {
            throw new BadRequestException(BaseException.HANDOFF_ALREADY_MANUAL);
        }
    }

    public void checkIsManual(Boolean isAuto) {
        if (isAuto) {
            throw new BadRequestException(BaseException.HANDOFF_ALREADY_AUTO);
        }
    }

}
