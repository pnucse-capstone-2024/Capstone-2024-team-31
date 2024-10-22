package com.wasin.wasin._core.exception.error;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.CustomException;
import com.wasin.wasin._core.util.ApiUtils;
import org.springframework.http.HttpStatus;


// 권한 없음 403
public class ForbiddenException extends CustomException {

    public ForbiddenException(BaseException exception){
        super(exception.getMessage());
    }

    @Override
    public ApiUtils.ApiResult<?> body() {
        return ApiUtils.error(getMessage(), status());
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.FORBIDDEN;
    }
}
